"""Returns the exact win rate for a given final_strategy.

Expects the following files in the current directory:

hog.py
dice.py

Because the state space for this game is relatively small, it 
can be computed to a large recursive depth, giving exact win rates.

Questions/comments: kvchen@berkeley.edu
"""

__version__ = '1.9'

# Just copied straight from the grader, no need for rewrites :)
try:
    import hog     # Student submission
except (SyntaxError, IndentationError) as e:
    import traceback
    print('Unfortunately, the exact win rate cannot be computed' +
          'because your program contains a syntax error:')
    traceback.print_exc(limit=1)
    exit(1)

from dice import make_test_dice, four_sided, six_sided
import urllib.request, urllib.error
import re
from math import factorial, floor
from datetime import datetime

# Blatantly stolen :(
def check_for_updates():
    print('You are running hog_exact.py version', __version__)
    print('========================================')
    index = 'https://raw.github.com/kvchen/hog_exact/master/'
    try:
        remote_hog_exact = urllib.request.urlopen(index + 'hog_exact.py').read().decode('utf-8')
    except urllib.error.URLError:
        print("Couldn't read from GitHub, check your network settings.")
        return
    remote_version = re.search("__version__ = '(.*)'", remote_hog_exact)
    if remote_version and remote_version.group(1) > __version__:
        print('There is a new version available for download (v%s)' % remote_version.group(1))
        prompt = input('Do you want to automatically download these files? [y/n]: ')
        if 'y' in prompt.lower():
            with open('hog_exact.py', 'w') as new:
                new.write(remote_hog_exact)
                print('hog_exact.py updated, please rerun the script.')
            exit(0)
        else:
            print('You can download the new win rate calculator from the following link:')
            print('\t' + index + 'hog_exact.py')
            print()

def nCr(n,r):
    """Returns the mathematical value of the combination n choose r,
    expanded to minimize rounding error.
    """
    assert n >= 0
    assert 0 <= r <= n

    c = int(1)
    denom = 1
    for (num,denom) in zip(range(n,n-r,-1), range(1,r+1,1)):
        c = (c * num) // denom
    return c

def generate_dice_table():
    """Creates a look-up table of all possible sums and their respective
    probabilities.

    Returns a dictionary of values that can be accessed by:
    generated_table[dice_type][num_rolls][total]
    """
    dice_table = {}
    for dice_type in (four_sided, six_sided):
        dice_table[dice_type] = {}
        sides = (4, 6)[dice_type == six_sided]

        for num_rolls in range(0, 11):
            dice_table[dice_type][num_rolls] = {}
            n = num_rolls
            if not(n):
                dice_table[dice_type][num_rolls] = {0: float(1)}

            # Calculates probabilities for sums of each number of rolls
            for possible_sum in range(1, sides*n+1):
                # Probability of getting a 1 on any one of n rolls
                if possible_sum==1:
                    prb = 1 - float((sides - 1) / sides)**n

                # Probability of getting a non-one total
                else:
                    combination = []
                    s, k = sides - 1, possible_sum - num_rolls
                    for i in range(0, floor((k-n)/s)+1):
                        combination.append(((-1)**i) * 
                            nCr(n, i) * nCr(k - s*i - 1, n - 1))
                    prb = sum(combination) / (sides**n)
                    #prb = sum(combination) * ((float(sides - 1) / sides)**n) * float((1 / (s**n)))
                dice_table[dice_type][num_rolls][possible_sum] = prb
    return dice_table

def p2_strategy(score, opponent_score):
    return 5

def calculate_win_rate(p1_strategy, p2_strategy, swap = False):
    """Calculates the true win rate for p1 in a scenario where p1_strategy
    takes precedence over p2_strategy in making the first move unless swap 
    is True, in which case p2 begins the game.
    """
    SCORE_SEQUENCE_MAP = {}
    
    def calculate_roll_nodes(p1 = 0, p2 = 0, who = swap):
        """A recursive search for which branches result in a p1 win.
        Calculates the true win rate (SCORE_SEQUENCE_MAP[(0,0,0)])
        """
        nonlocal SCORE_SEQUENCE_MAP
        if (p1, p2, who) not in SCORE_SEQUENCE_MAP:
            if (p1 >= hog.GOAL_SCORE):
                node_value = 1
            elif (p2 >= hog.GOAL_SCORE):
                node_value = 0
            else:
                num_rolls = (p1_strategy, p2_strategy)[who]((p1, p2)[who], (p2, p1)[who])

                # Checks for hog wild rule
                dice = (four_sided, six_sided)[(p1 + p2) % 7 != 0]
                
                # Loops through all possible number of rolls
                branches = []
                for possible_sum in dice_table[dice][num_rolls]:
                    branch_prb = dice_table[dice][num_rolls][possible_sum]

                    home, away = (p1, p2)[who], (p2, p1)[who]

                    # Checks for free bacon
                    if not(num_rolls):
                        home += max([int(digit) for digit in str(away)]) + 1
                    else:
                        home += possible_sum

                    # Checks for swine swap
                    if (home * 2 == away) or (away * 2 == home):
                        home, away = away, home

                    branches.append(branch_prb * calculate_roll_nodes((home, away)[who], (away, home)[who], 1 - who))
                node_value = sum(branches)
            SCORE_SEQUENCE_MAP[(p1, p2, who)] = node_value
        return SCORE_SEQUENCE_MAP[(p1, p2, who)]
    calculate_roll_nodes()
    return SCORE_SEQUENCE_MAP[(0,0,swap)]

if __name__ == '__main__':
    check_for_updates()
    startTime = datetime.now()
    print("Generating dice probability tables...")
    dice_table = generate_dice_table()
    print("Generating roll path probability tables...")
    print("Calculating exact win rate...")
    true_win_rate = (calculate_win_rate(hog.final_strategy, p2_strategy) + 
        calculate_win_rate(hog.final_strategy, p2_strategy, swap = True)) / 2
    print("Script finished in %s" % str(datetime.now() - startTime))
    print("True average win rate of final_strategy is %s" % true_win_rate)