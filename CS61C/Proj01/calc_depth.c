/*
 * PROJ1-1: YOUR TASK A CODE HERE
 *
 * Feel free to define additional helper functions.
 */

#include "calc_depth.h"
#include "utils.h"
#include <math.h>
#include <limits.h>
#include <stdio.h>

/* Implements the normalized displacement function */
unsigned char normalized_displacement(int dx, int dy,
        int maximum_displacement) {

    double squared_displacement = dx * dx + dy * dy;
    double normalized_displacement = round(255 * sqrt(squared_displacement) / sqrt(2 * maximum_displacement * maximum_displacement));
    return (unsigned char) normalized_displacement;

}

void calc_depth(unsigned char *depth_map, unsigned char *left,
        unsigned char *right, int image_width, int image_height,
        int feature_width, int feature_height, int maximum_displacement) {
	int x = 0;
	int y = 0;
	int lower_x = 0;
	int upper_x = 0;
	int lower_y = 0;
	int upper_y = 0;
	for (int i = 0; i < image_width * image_height; i++) {
		x = i % image_width;
		y = i / image_width;
		if (maximum_displacement == 0) {
			depth_map[i] = 0;
		}
		else if (x - feature_width < 0 || x + feature_width > image_width - 1 || y - feature_height < 0 || y + feature_height > image_height - 1) {
			depth_map[i] = 0;
		}
		else {
			if (x - maximum_displacement < 0) {
				lower_x = 0;
			}
			else {
				lower_x = x - maximum_displacement;
			}
			if ( x + maximum_displacement > image_width - 1) {
				upper_x = image_width - 1;
			}
			else {
				upper_x = x + maximum_displacement;
			}
			if (y - maximum_displacement < 0) {
				lower_y = 0;
			}
			else {
				lower_y = y - maximum_displacement;				
			}
			if (y + maximum_displacement > image_height - 1) {
				upper_y = image_height - 1;
			}
			else {
				upper_y = y + maximum_displacement;
			}
			int smallest_x = -1;
			int smallest_y = -1;
			unsigned int euclidean = -1;
			for (int j = lower_y; j <= upper_y; j++) {
				for (int k = lower_x; k <= upper_x; k++) {
					int sum = 0;
					if (k - feature_width < 0 || k + feature_width > image_width - 1 || j - feature_height < 0 || j + feature_height > image_height - 1) {
						continue;
					}
					for (int l = -feature_height; l <= feature_height; l++) {
						for (int m = -feature_width; m <= feature_width; m++) {
							sum += pow((left[(y + l) * image_width + (x + m)] - right[(j + l) * image_width + (k + m)]), 2);
						}
					}
					if (sum < euclidean) {
						euclidean = sum;
						smallest_y = j;
						smallest_x = k;
					}
					else if (sum == euclidean) {
						if (normalized_displacement(x - smallest_x, y - smallest_y, maximum_displacement) > normalized_displacement(x - k, y - j, maximum_displacement)) {
							smallest_y = j;
							smallest_x = k;
						}
					}
				}
			}
			depth_map[i] = normalized_displacement(x - smallest_x, y - smallest_y, maximum_displacement);
		}
	}
}

