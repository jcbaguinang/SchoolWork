from pyspark import SparkContext
import Sliding, argparse
def bfs_map(value):
    """ YOUR CODE HERE """
    result = []
    if value[1] == level - 1:
        result = Sliding.children(WIDTH, HEIGHT, Sliding.hash_to_board(WIDTH, HEIGHT, value[0]))
        for i in range(0, len(result)):
            result[i] = (Sliding.board_to_hash(WIDTH, HEIGHT, result[i]), level)
    result.append(value)
    return result

def bfs_reduce(value1, value2):
    """ YOUR CODE HERE """
    if value1 < value2:
        return value1
    else:
        return value2

def solve_puzzle(master, output, height, width, slaves):
    global HEIGHT, WIDTH, level
    HEIGHT=height
    WIDTH=width
    level = 0

    sc = SparkContext(master, "python")
    sol = Sliding.board_to_hash(WIDTH, HEIGHT, Sliding.solution(WIDTH, HEIGHT))


    """ YOUR CODE HERE """
    count = 1
    new_count = 2
    output_rdd = sc.parallelize([(sol, 0)])
    while count != new_count:
        level = level + 1
        output_rdd = output_rdd.flatMap(bfs_map).reduceByKey(bfs_reduce)
        if level % 8 == 0:
            output_rdd = output_rdd.partitionBy(PARTITION_COUNT)
            count = new_count
            new_count = output_rdd.count()
    output_rdd.coalesce(slaves).saveAsTextFile(output)
    sc.stop()



""" DO NOT EDIT PAST THIS LINE

You are welcome to read through the following code, but you
do not need to worry about understanding it.
"""

def main():
    """
    Parses command line arguments and runs the solver appropriately.
    If nothing is passed in, the default values are used.
    """
    parser = argparse.ArgumentParser(
            description="Returns back the entire solution graph.")
    parser.add_argument("-M", "--master", type=str, default="local[8]",
            help="url of the master for this job")
    parser.add_argument("-O", "--output", type=str, default="solution-out",
            help="name of the output file")
    parser.add_argument("-H", "--height", type=int, default=2,
            help="height of the puzzle")
    parser.add_argument("-W", "--width", type=int, default=2,
            help="width of the puzzle")
    parser.add_argument("-S", "--slaves", type=int, default=6,
            help="number of slaves executing the job")
    args = parser.parse_args()

    global PARTITION_COUNT
    PARTITION_COUNT = args.slaves * 16

    # call the puzzle solver
    solve_puzzle(args.master, args.output, args.height, args.width, args.slaves)

# begin execution if we are running this file directly
if __name__ == "__main__":
    main()
