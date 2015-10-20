// CS 61C Fall 2014 Project 3

// include SSE intrinsics
#if defined(_MSC_VER)
#include <intrin.h>
#elif defined(__GNUC__) && (defined(__x86_64__) || defined(__i386__))
#include <x86intrin.h>
#endif

#include "calcDepthOptimized.h"
#include "calcDepthNaive.h"

#define ABS(x) (((x) < 0) ? (-(x)) : (x))

void calcDepthOptimized(float *depth, float *left, float *right, int imageWidth, int imageHeight, int featureWidth, int featureHeight, int maximumDisplacement)
{	
	for (int x = 0; x < imageWidth; x++)
	{
		for (int y = 0; y < imageHeight; y++)
		{
			if ((y < featureHeight) || (y >= imageHeight - featureHeight) || (x < featureWidth) || (x >= imageWidth - featureWidth))
			{
				depth[y * imageWidth + x] = 0;
				continue;
			}

			float minimumSquaredDifference = -1;
			int minimumDy = 0;
			int minimumDx = 0;
			for (int dx = -maximumDisplacement; dx <= maximumDisplacement; dx++)
			{
				for (int dy = -maximumDisplacement; dy <= maximumDisplacement; dy++)
				{
					if (y + dy - featureHeight < 0 || y + dy + featureHeight >= imageHeight || x + dx - featureWidth < 0 || x + dx + featureWidth >= imageWidth)
					{
						continue;
					}
					int boxX;
					float squaredDifference = 0;
					__m128 sum = _mm_setzero_ps();
					float squaredDifferenceInt[4];
					/*for (boxX = -featureWidth; boxX + 8 <= featureWidth; boxX+= 8)
					{
						for (int boxY = -featureHeight; boxY <= featureHeight; boxY++)
						{
							int leftX = x + boxX;
							int leftY = y + boxY;
							int rightX = x + dx + boxX;
							int rightY = y + dy + boxY;
							__m128 left1 = _mm_loadu_ps(left + (leftY * imageWidth + leftX));
							__m128 right1 = _mm_loadu_ps(right + (rightY * imageWidth + rightX));
							__m128 left2 = 	_mm_loadu_ps(left + (leftY * imageWidth + leftX) + 4);	
							__m128 right2 = _mm_loadu_ps(right + (rightY * imageWidth + rightX) + 4);	
							__m128 sqdiff = _mm_sub_ps(left1, right1);
							sqdiff = _mm_mul_ps(sqdiff, sqdiff);
							sum = _mm_add_ps(sqdiff, sum);
							sqdiff = _mm_sub_ps(left2, right2);
							sqdiff = _mm_mul_ps(sqdiff, sqdiff);
							sum = _mm_add_ps(sqdiff, sum);
						}
					}
					_mm_storeu_ps(squaredDifferenceInt, sum);
					squaredDifference += squaredDifferenceInt[0] + squaredDifferenceInt[1] + squaredDifferenceInt[2] + squaredDifferenceInt[3];
					if (squaredDifference > minimumSquaredDifference && minimumSquaredDifference != -1) {
						continue;
					} */
					for (int boxX2 = -featureWidth; boxX2 + 4 <= featureWidth; boxX2+= 4)
					{
						for (int boxY = -featureHeight; boxY <= featureHeight; boxY++)
						{
							int leftX = x + boxX2;
							int leftY = y + boxY;
							int rightX = x + dx + boxX2;
							int rightY = y + dy + boxY;
							__m128 left2 = _mm_loadu_ps(left + (leftY * imageWidth + leftX));
							__m128 right2 = _mm_loadu_ps(right + (rightY * imageWidth + rightX));			
							__m128 sqdiff = _mm_sub_ps(left2, right2);
							sqdiff = _mm_mul_ps(sqdiff, sqdiff);
							sum = _mm_add_ps(sqdiff, sum);
						}
					}
					_mm_storeu_ps(squaredDifferenceInt, sum);
					squaredDifference += squaredDifferenceInt[0] + squaredDifferenceInt[1] + squaredDifferenceInt[2] + squaredDifferenceInt[3];
					if (squaredDifference > minimumSquaredDifference && minimumSquaredDifference != -1) {
						continue;
					}
					else if (featureWidth % 2 == 0) {
						for (int j = -featureHeight; j <= featureHeight; j++) {
							int leftX = x + featureWidth;
							int leftY = y + j;
							int rightX = x + dx + featureWidth;
							int rightY = y + dy + j;
							float difference = left[leftY * imageWidth + leftX] - right[rightY * imageWidth + rightX];
							squaredDifference += difference * difference;
						}
					}
					else {
						__m128 sum = _mm_setzero_ps();
						for (int j = -featureHeight; j <= featureHeight; j++) {
							int leftX = x + featureWidth - 3;
							int leftY = y + j;
							int rightX = x + dx + featureWidth - 3;
							int rightY = y + dy + j;
							__m128 left2 = _mm_loadu_ps(left + (leftY * imageWidth + leftX));
							__m128 right2 = _mm_loadu_ps(right + (rightY * imageWidth + rightX));
							__m128 sqdiff = _mm_sub_ps(left2, right2);
							sqdiff = _mm_mul_ps(sqdiff, sqdiff);
							sum = _mm_add_ps(sqdiff, sum);
						}
						_mm_storeu_ps(squaredDifferenceInt, sum);
						squaredDifference += squaredDifferenceInt[1] + squaredDifferenceInt[2] + squaredDifferenceInt[3];
					}
					if ((minimumSquaredDifference == -1) || ((minimumSquaredDifference == squaredDifference) && (displacementNaive(dx, dy) < displacementNaive(minimumDx, minimumDy))) || (minimumSquaredDifference > squaredDifference))
					{
						minimumSquaredDifference = squaredDifference;
						minimumDx = dx;
						minimumDy = dy;
					}
				}
			}
			if (minimumSquaredDifference != -1 && maximumDisplacement != 0)
			{
				depth[y * imageWidth + x] = displacementNaive(minimumDx, minimumDy);
			}
			else
			{
				depth[y * imageWidth + x] = 0;
			}
		}
	}
}
