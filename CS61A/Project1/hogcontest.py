"""The Game of Hog."""

from dice import four_sided, six_sided, make_test_dice
from ucb import main, trace, log_current_line, interact

GOAL_SCORE = 100 # The goal of Hog is to score 100 points.

######################
# Phase 1: Simulator #
######################

# Taking turns

def roll_dice(num_rolls, dice=six_sided):
    """Roll DICE for NUM_ROLLS times.  Return either the sum of the outcomes,
    or 1 if a 1 is rolled (Pig out). This calls DICE exactly NUM_ROLLS times.

    num_rolls:  The number of dice rolls that will be made; at least 1.
    dice:       A zero-argument function that returns an integer outcome.
    """
    # These assert statements ensure that num_rolls is a positive integer.
    assert type(num_rolls) == int, 'num_rolls must be an integer.'
    assert num_rolls > 0, 'Must roll at least once.'
    "*** YOUR CODE HERE ***"
    tot = 0
    count = 1
    one = 4
    while (count <= num_rolls):
        result = dice()
        tot = tot + result
        if(result == 1):
            one = 1
        count = count + 1
    if(one == 1):
        return 1
    else:
        return tot
def take_turn(num_rolls, opponent_score, dice=six_sided):
    """Simulate a turn rolling NUM_ROLLS dice, which may be 0 (Free bacon).

    num_rolls:       The number of dice rolls that will be made.
    opponent_score:  The total score of the opponent.
    dice:            A function of no args that returns an integer outcome."""
    assert type(num_rolls) == int, 'num_rolls must be an integer.'
    assert num_rolls >= 0, 'Cannot roll a negative number of dice.'
    assert num_rolls <= 10, 'Cannot roll more than 10 dice.'
    assert opponent_score < 100, 'The game should be over.'
    "*** YOUR CODE HERE ***"
    if(num_rolls == 0):
        return max((opponent_score//10), (opponent_score % 10)) + 1
    else:
        return roll_dice(num_rolls, dice)
# Playing a game

def select_dice(score, opponent_score):
    """Select six-sided dice unless the sum of SCORE and OPPONENT_SCORE is a
    multiple of 7, in which case select four-sided dice (Hog wild).

    >>> select_dice(4, 24) == four_sided
    True
    >>> select_dice(16, 64) == six_sided
    True
    >>> select_dice(0, 0) == four_sided
    True
    """
    "*** YOUR CODE HER***"
    if((score + opponent_score) % 7 == 0):
        return four_sided
    else:
        return six_sided

def other(who):
    """Return the other player, for a player WHO numbered 0 or 1.

    >>> other(0)
    1
    >>> other(1)
    0
    """
    return 1 - who

def play(strategy0, strategy1, goal=GOAL_SCORE):
    """Simulate a game and return the final scores of both players, with
    Player 0's score first, and Player 1's score second.

    A strategy is a function that takes two total scores as arguments
    (the current player's score, and the opponent's score), and returns a
    number of dice that the current player will roll this turn.

    strategy0:  The strategy function for Player 0, who plays first.
    strategy1:  The strategy function for Player 1, who plays second.
    """
    who = 0  # Which player is about to take a turn, 0 (first) or 1 (second)
    score, opponent_score = 0, 0
    "*** YOUR CODE HERE ***"
    while (score < GOAL_SCORE and opponent_score < GOAL_SCORE):
        if (who == 0):
            score = score + take_turn(strategy0(score, opponent_score), opponent_score, select_dice(score, opponent_score))
        else:
            opponent_score = opponent_score + take_turn(strategy1(opponent_score, score), score, select_dice(opponent_score, score))
        if(score == opponent_score//2 and opponent_score % 2 == 0 or opponent_score == score/2 and score % 2 == 0):
            x = opponent_score
            opponent_score = score
            score = x
        who = other(who)    
    return score, opponent_score  # You may wish to change this line.


#######################
# Phase 2: Strategies #
#######################

# Basic Strategy

BASELINE_NUM_ROLLS = 5
BACON_MARGIN = 8

def always_roll(n):
    """Return a strategy that always rolls N dice.

    A strategy is a function that takes two total scores as arguments
    (the current player's score, and the opponent's score), and returns a
    number of dice that the current player will roll this turn.

    >>> strategy = always_roll(5)
    >>> strategy(0, 0)
    5
    >>> strategy(99, 99)
    5
    """
    def strategy(score, opponent_score):
        return n
    return strategy

# Experiments

def make_averaged(fn, num_samples=50000):
    """Return a function that returns the average_value of FN when called.

    To implement this function, you will have to use *args syntax, a new Python
    feature introduced in this project.  See the project description.

    >>> dice = make_test_dice(3, 1, 5, 6)
    >>> averaged_dice = make_averaged(dice, 1000)
    >>> averaged_dice()
    3.75
    >>> make_averaged(roll_dice, 1000)(2, dice)
    6.0

    In this last example, two different turn scenarios are averaged.
    - In the first, the player rolls a 3 then a 1, receiving a score of 1.
    - In the other, the player rolls a 5 and 6, scoring 11.
    Thus, the average value is 6.0.
    """
    "*** YOUR CODE HERE ***"
    def average_value(*args):
        count = 1
        total = 0
        while (count <= num_samples):
            total = fn(*args) + total
            count += 1
        average = total / num_samples
        return average
    return average_value

def max_scoring_num_rolls(dice=six_sided):
    """Return the number of dice (1 to 10) that gives the highest average turn
    score by calling roll_dice with the provided DICE.  Print all averages as in
    the doctest below.  Assume that dice always returns positive outcomes.

    >>> dice = make_test_dice(3)
    >>> max_scoring_num_rolls(dice)
    1 dice scores 3.0 on average
    2 dice scores 6.0 on average
    3 dice scores 9.0 on average
    4 dice scores 12.0 on average
    5 dice scores 15.0 on average
    6 dice scores 18.0 on average
    7 dice scores 21.0 on average
    8 dice scores 24.0 on average
    9 dice scores 27.0 on average
    10 dice scores 30.0 on average
    10
    """
    "*** YOUR CODE HERE ***"
    count = 1
    max = 0.0
    max_count = 0
    while (count <= 10):
        count_avg = make_averaged(roll_dice, 1000)(count, dice)
        if(count_avg > max):
            max = count_avg
            max_count = count
        print (str(count) + ' dice scores ' + str(count_avg) + ' on average')
        count = count + 1
    return max_count

def winner(strategy0, strategy1):
    """Return 0 if strategy0 wins against strategy1, and 1 otherwise."""
    score0, score1 = play(strategy0, strategy1)
    if score0 > score1:
        return 0
    else:
        return 1

def average_win_rate(strategy, baseline=always_roll(BASELINE_NUM_ROLLS)):
    """Return the average win rate (0 to 1) of STRATEGY against BASELINE."""
    win_rate_as_player_0 = 1 - make_averaged(winner)(strategy, baseline)
    win_rate_as_player_1 = make_averaged(winner)(baseline, strategy)
    return (win_rate_as_player_0 + win_rate_as_player_1) / 2 # Average results

def run_experiments():
    """Run a series of strategy experiments and report results."""
    if True: # Change to False when done finding max_scoring_num_rolls
        six_sided_max = max_scoring_num_rolls(six_sided)
        print('Max scoring num rolls for six-sided dice:', six_sided_max)
        four_sided_max = max_scoring_num_rolls(four_sided)
        print('Max scoring num rolls for four-sided dice:', four_sided_max)

    if False: # Change to True to test always_roll(8)
        print('always_roll(8) win rate:', average_win_rate(always_roll(8)))
    

    if False: # Change to False to test bacon_strategy
        print('bacon_strategy win rate:', average_win_rate(bacon_strategy))

    if False: # Change to False to test swap_strategy
        print('swap_strategy win rate:', average_win_rate(swap_strategy))

    if True: # Change to False to test final_strategy
        print('final_strategy win rate:', average_win_rate(final_strategy, always_roll(5)))

    "*** You may add additional experiments as you wish ***"

# Strategies

def bacon_strategy(score, opponent_score):
    """This strategy rolls 0 dice if that gives at least BACON_MARGIN points,
    and rolls BASELINE_NUM_ROLLS otherwise.

    >>> bacon_strategy(0, 0)
    5
    >>> bacon_strategy(70, 50)
    5
    >>> bacon_strategy(50, 70)
    0
    """
    "*** YOUR CODE HERE ***"
    if(max((opponent_score//10), (opponent_score % 10)) + 1 >= BACON_MARGIN):
        return 0
    else:
        return BASELINE_NUM_ROLLS


def swap_strategy(score, opponent_score):
    """This strategy rolls 0 dice when it would result in a beneficial swap and
    rolls BASELINE_NUM_ROLLS if it would result in a harmful swap. It also rolls
    0 dice if that gives at least BACON_MARGIN points and rolls
    BASELINE_NUM_ROLLS otherwise.

    >>> swap_strategy(23, 60) # 23 + (1 + max(6, 0)) = 30: Beneficial swap
    0
    >>> swap_strategy(27, 18) # 27 + (1 + max(1, 8)) = 36: Harmful swap
    5
    >>> swap_strategy(50, 80) # (1 + max(8, 0)) = 9: Lots of free bacon
    0
    >>> swap_strategy(12, 12) # Baseline
    5
    """
    "*** YOUR CODE HERE ***"
    bacon_number = 1 + max((opponent_score//10), (opponent_score % 10))
    if((score + bacon_number) == opponent_score//2 and opponent_score % 2 == 0):
        return 0
    elif((score + bacon_number)//2 == opponent_score and (score + bacon_number)%2 == 0):
        return BASELINE_NUM_ROLLS
    elif(bacon_number >= BACON_MARGIN):
        return 0
    else:
        return BASELINE_NUM_ROLLS



def final_strategy(score, opponent_score):
    """Write a brief description of your final strategy.

    *** make em four six_sided
        if less do more than 6 if higher do 6
        if four do something
        if 6 do something ***
    """
    "*** YOUR CODE HERE ***"
    hog_wild=(score+opponent_score)%7
    
    bacon_number = 1 + max((opponent_score//10), (opponent_score % 10))
    
    """if dat bacon numba wins i use it
    """

    if ((score+bacon_number)>=100 and ((score+bacon_number)//2!=opponent_score or (score+bacon_number)%2!=0)):
        return 0

    """if i can win i play it safe man
    """

    """this is if i have 4 side
    """
    
    def ways_to_roll_at_least(k,n):
        if k<=0:
            return pow(5,n)
        elif n==0:
            return 0
        else:
            total, d = 0, 2
            while d<=6:
                total=total+ways_to_roll_at_least(k-d,n-1)
                d=d+1
            return total

    def ways_to_roll_at_least_4(k,n):
        if k<=0:
            return pow(3,n)
        elif n==0:
            return 0
        else:
            total, d = 0, 2
            while d<=4:
                total=total+ways_to_roll_at_least(k-d,n-1)
                d=d+1
            return total

    def chance_to_roll_at_least(k,n):
        return ways_to_roll_at_least(k,n) / pow(6,n)

    def chance_to_roll_at_least_4(k,n):
        return ways_to_roll_at_least_4(k,n) / pow(4,n)

    def roll_at_least(k,n):
        if roll_dice(n) >=k:
            return 1
        else:
            return 0

    
    
    
    
    
    

    if (hog_wild==0):
        if (score>=97 and (opponent_score<50 or opponent_score>50)):
            return 1
        if (score>=95 and (opponent_score<48 or opponent_score>51)):
            return 2
        if (score>=92 and (opponent_score<47 or opponent_score>55) and (((score+1)//2!=opponent_score) or (score+1)%2!=0)):
            return 3
        if (score>=90 and (opponent_score<47 or opponent_score>55) and (((score+1)//2!=opponent_score) or (score+1)%2!=0)):
            return 4
        
    
    
    if (score>=97 and (opponent_score<48 or opponent_score>53)):
        return 1
    if (score>=94 and (opponent_score<47 or opponent_score>52)):
        return 2
    if (score>=91 and (opponent_score<46 or opponent_score>52) and (((score+1)//2!=opponent_score) or (score+1)%2!=0)):
        return 3
    if (score>=89 and (opponent_score<43 or opponent_score>54) and (((score+1)//2!=opponent_score) or (score+1)%2!=0)):
        return 4
    
    """
    if score>87 and hog_wild==0:
        highest_roll=1
        highest_prob=0
        roll=1
        while roll<11:
            if chance_to_roll_at_least_4(100-score,roll)>highest_prob:
                highest_prob=chance_to_roll_at_least_4(100-score, roll)
                highest_roll=roll
            roll+=1
        if highest_prob>.45 and opponent_score<=score and score//2>(opponent_score-highest_roll*2) and (((score+1)//2!=opponent_score) or (score+1)%2!=0):
            return highest_roll
    
    if score>85 and hog_wild!=0:
        highest_roll=1
        highest_prob=0
        roll=1
        while roll<11:
            if chance_to_roll_at_least(100-score,roll)>highest_prob:
                highest_prob=chance_to_roll_at_least(100-score, roll)
                highest_roll=roll
            roll+=1
        if highest_prob>.45 and opponent_score<=score and score//2>(opponent_score-highest_roll*2) and (((score+1)//2!=opponent_score) or (score+1)%2!=0):
            return highest_roll
    """
    

    """
    if (score==0 and opponent_score<10 and opponent_score>5):
        return 0
    """

    """ if i can swap by choosing 0 i do it
    """
    """
    if (score<6 and opponent_score<6):
        return 10
    """

    """
    THIS IS INTERESTING!!!!!!!
    """
    
    
    if (score<6 and opponent_score<3):
        return 3
    if score<8 and opponent_score<6:
        return 4
    
    

    if((score + bacon_number) == opponent_score//2 and opponent_score % 2 == 0):
        return 0
    

    """ if choosing 0
     makes me do a bad swap i dont do it
    """
    
    """
    if((score + bacon_number)//2 == opponent_score and (score + bacon_number)%2 == 0):
        return 5
    """
    """ if +1 swaps i do it
    """
    
    if((score + 1) == opponent_score//2 and opponent_score % 2 == 0):
        return -1
   
    if((score + bacon_number + 1) == (opponent_score + 1)//2 and (opponent_score+1)%2==0):
        return 0

    if((score + 1 + (max(((opponent_score+1)//10), ((opponent_score+1) % 10))) + 1) == (opponent_score + 1)//2 and (opponent_score+1)%2==0):
        return -1


    """
     if dat bacon numba is pro i do it
    """
    if (opponent_score<=100 and (opponent_score-score)<9):
        if (((score+bacon_number)+opponent_score)%7==0 and bacon_number>4 and ((score+bacon_number)//2!=opponent_score or (score+bacon_number)%2!=0)and (((score + bacon_number+1)//2 != opponent_score or (score + bacon_number+1)%2 != 0))):
            return 0
    if (opponent_score<=80 and (opponent_score-score)<9):
        if hog_wild==0:
            if (bacon_number >= 5 and ((score+bacon_number)//2!=opponent_score or (score+bacon_number)%2!=0)and (((score + bacon_number)//2 != (opponent_score+1) or (score + bacon_number+1)%2 != 0))):
                return 0
        else:
            if (bacon_number >= 6 and ((score+bacon_number)//2!=opponent_score or (score+bacon_number)%2!=0)and (((score + bacon_number)//2 != (opponent_score+1) or (score + bacon_number+1)%2 != 0))):
                return 0
    """
    if score>4:
    """

    
    if ((score+1)//2==opponent_score and (score+1)%2==0):   
        return 2
    if ((score+1)//2==(opponent_score+1) and (score+1)%2==0):   
        return 2
    if ((score+1)//2==(opponent_score+bacon_number) and (score+1)%2==0):   
        return 2
    if ((score+2)//2==(opponent_score+2) and (score+2)%2==0):   
        return 2
    
    

    
    
    """
    if the bacon number is good and it makes need a +1 to swap me  a good swap i do it
    """

    
    if ((score + bacon_number) == (opponent_score+1)//2 and (opponent_score+1) % 2 == 0 and bacon_number>2):
        return 0
    

    """
    if(((score + bacon_number+1)//2 != opponent_score or (score + bacon_number+1)%2 != 0)):
        return 5
    """

    """
    if (opponent_score>10):   
    """
    if ((opponent_score+1)//2== (score+1) and (opponent_score +1)%2 == 0 and (score+opponent_score+1)%7==0):
        return -1
    """
    if ((score+2)//2==opponent_score and (score +2)%2 == 0):
        return 10
    """

    """
    if ((score+1)+opponent_score)%7==0:
        return 10
    """
    

    

    """ if dat bacon numba gives him a four side i do it
    """
    
    if (opponent_score>15 and opponent_score//2-score>2):
        if (opponent_score//2-score)==2:
            return 1
        if (opponent_score//2-score)==3:
            return 1
        if (opponent_score//2-score)==4:
            return 1
        if (opponent_score//2-score)==5:
            return 2
        if (opponent_score//2-score)==6:
            return 2
        if (opponent_score//2-score)==7:
            return 2
        if (opponent_score//2-score)==8:
            return 3
        if (opponent_score//2-score)==9:
            return 3
        if (opponent_score//2-score)==10:
            return 3 
        if (opponent_score//2-score)==11:
            return 3
        if (opponent_score//2-score)==12:
            return 4
        if (opponent_score//2-score)==13:
            return 4
        if (opponent_score//2-score)==14:
            return 4
        if (opponent_score//2-score)==15:
            return 4
        if (opponent_score//2-score)==16:
            return 5
        if (opponent_score//2-score)==17:
            return 5
        if (opponent_score//2-score)==18:
            return 5
        if (opponent_score//2-score)==19:
            return 6
        if (opponent_score//2-score)==20:
            return 6
        if (opponent_score//2-score)==21:
            return 6
        if (opponent_score//2-score)==22:
            return 6
        if (opponent_score//2-score)==23:
            return 7
        if (opponent_score//2-score)==24:
            return 7
        if (opponent_score//2-score)==25:
            return 7
        if (opponent_score//2-score)==26:
            return 7
        if (opponent_score//2-score)==27:
            return 8
        if (opponent_score//2-score)==28:
            return 8
        if (opponent_score//2-score)==29:
            return 8
        if (opponent_score//2-score)==30:
            return 8


        

    
    



    if opponent_score<90 and opponent_score > 80:
        if((opponent_score//2) > score and (opponent_score//2) < (score+3) and score<opponent_score):
            return 10
    if opponent_score<81 and opponent_score > 75:        
        if((opponent_score//2-100) > score and (opponent_score//2) < (score+2) and score<opponent_score):
            return -1
    if opponent_score<76 and opponent_score > 70:
        if((opponent_score//2 ) > score and (opponent_score//2) < (score+2) and score<opponent_score):
            return 10
    if opponent_score<71 and opponent_score > 65:        
        if((opponent_score//2 ) > score and (opponent_score//2) < (score+2) and score<opponent_score):
            return -1
    if opponent_score<66 and opponent_score > 60:
        if((opponent_score//2) > score and (opponent_score//2) < (score+2) and score<opponent_score):
            return 10
    if opponent_score<61 and opponent_score > 55:        
        if((opponent_score//2) > score and (opponent_score//2) < (score+2) and score<opponent_score):
            return -1
    if opponent_score<56 and opponent_score > 50:
        if((opponent_score//2 ) > score and (opponent_score//2) < (score+2) and score<opponent_score):
            return 10
            
    if opponent_score<51 and opponent_score > 45:        
        if((opponent_score//2 +9) > score and (opponent_score//2) < (score+1) and score<opponent_score):
            return -1
            
    if opponent_score<46 and opponent_score > 40:
        if((opponent_score//2+5 ) > score and (opponent_score//2) < (score+2) and score<opponent_score):
            return 10
            
    if opponent_score<41 and opponent_score > 35:        
        if((opponent_score//2 + 9) > score and (opponent_score//2) < (score+2)and score<opponent_score):
            return -1
            
    if opponent_score<36 and opponent_score > 30:
        if((opponent_score//2 + 13) > score and (opponent_score//2) < (score+2) and score<opponent_score):
            return 10

         
    if opponent_score<34 and opponent_score > 19:        
        if((opponent_score//2 +15) > score and (opponent_score//2) < (score+2) ):
            return -1
    if opponent_score<21 and opponent_score > 15:
        if((opponent_score//2 + 15) > score and (opponent_score//2) < (score+2) and score<opponent_score):
            return 10

         
    if opponent_score<16 and opponent_score > 10:        
        if((opponent_score//2 + 12) > score and (opponent_score//2) < (score+2) ):
            return 10
    """
    if opponent_score<11 and opponent_score > 5:
        if((opponent_score//2 + 3) > score and (opponent_score//2) < (score+2) and score<opponent_score):
            return 10
    """

   
         
    
        

    
    if opponent_score<79 and opponent_score > 10:
        if(opponent_score//2 + 11 > score and opponent_score//2 < score):
            return 10
    
    
    if score<31 and opponent_score<60 and opponent_score>-1 and (opponent_score//2+15)>score and (opponent_score//2) < (score):
        return 10
    
    
    """
    if (opponent_score+score)==0:
        return 7
    """
    if hog_wild==0:
        if score < opponent_score-27:
            return 10
        if score < opponent_score-27:
            return 9
        if score < opponent_score-27:
            return 8
        if score < opponent_score-22:
            return 7
        if score < opponent_score-15:
            return 6 
        if score < opponent_score+20:
            return 5
        return 4

    

    if score<opponent_score-28:
        return 10

    
    if opponent_score>82 and opponent_score-30>score:
        return 8
    
    
    if score<opponent_score-24:
        return 8
    if score<opponent_score-20:
        return 7  
    
    if score < opponent_score-15:
        return 6
    if score < opponent_score-8:
        return 5
    """
    if score>=opponent_score:
        return 5
    """
    if score>=opponent_score+13:
        return 4
    """
    if score>=opponent_score+23:
        return 3
    """
    


    return 4 # Replace this statement


##########################
# Command Line Interface #
##########################

# Note: Functions in this section do not need to be changed.  They use features
#       of Python not yet covered in the course.

def get_int(prompt, min):
    """Return an integer greater than or equal to MIN, given by the user."""
    choice = input(prompt)
    while not choice.isnumeric() or int(choice) < min:
        print('Please enter an integer greater than or equal to', min)
        choice = input(prompt)
    return int(choice)

def interactive_dice():
    """A dice where the outcomes are provided by the user."""
    return get_int('Result of dice roll: ', 1)

def make_interactive_strategy(player):
    """Return a strategy for which the user provides the number of rolls."""
    prompt = 'Number of rolls for Player {0}: '.format(player)
    def interactive_strategy(score, opp_score):
        if player == 1:
            score, opp_score = opp_score, score
        print(score, 'vs.', opp_score)
        choice = get_int(prompt, 0)
        return choice
    return interactive_strategy

def roll_dice_interactive():
    """Interactively call roll_dice."""
    num_rolls = get_int('Number of rolls: ', 1)
    turn_total = roll_dice(num_rolls, interactive_dice)
    print('Turn total:', turn_total)

def take_turn_interactive():
    """Interactively call take_turn."""
    num_rolls = get_int('Number of rolls: ', 0)
    opp_score = get_int('Opponent score: ', 0)
    turn_total = take_turn(num_rolls, opp_score, interactive_dice)
    print('Turn total:', turn_total)

def play_interactive():
    """Interactively call play."""
    strategy0 = make_interactive_strategy(0)
    strategy1 = make_interactive_strategy(1)
    score0, score1 = play(strategy0, strategy1)
    print('Final scores:', score0, 'to', score1)

@main
def run(*args):
    """Read in the command-line argument and calls corresponding functions.

    This function uses Python syntax/techniques not yet covered in this course.
    """
    import argparse
    parser = argparse.ArgumentParser(description="Play Hog")
    parser.add_argument('--interactive', '-i', type=str,
                        help='Run interactive tests for the specified question')
    parser.add_argument('--run_experiments', '-r', action='store_true',
                        help='Runs strategy experiments')
    args = parser.parse_args()

    if args.interactive:
        test = args.interactive + '_interactive'
        if test not in globals():
            print('To use the -i option, please choose one of these:')
            print('\troll_dice', '\ttake_turn', '\tplay', sep='\n')
            exit(1)
        try:
            globals()[test]()
        except (KeyboardInterrupt, EOFError):
            print('\nQuitting interactive test')
            exit(0)
    elif args.run_experiments:
        run_experiments()
