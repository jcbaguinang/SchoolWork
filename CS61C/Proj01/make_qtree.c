/*
 * PROJ1-1: YOUR TASK B CODE HERE
 *
 * Feel free to define additional helper functions.
 */

#include <stdlib.h>
#include <stdio.h>
#include "quadtree.h"
#include "make_qtree.h"
#include "utils.h"

#define ABS(x) (((x) < 0) ? (-(x)) : (x))
qNode* quad_helper_NW(unsigned char *depth_map, int map_width, int x, int y);
qNode* quad_helper_NE(unsigned char *depth_map, int map_width, int x, int y);
qNode* quad_helper_SE(unsigned char *depth_map, int map_width, int x, int y);
qNode* quad_helper_SW(unsigned char *depth_map, int map_width, int x, int y);
qNode *depth_to_quad_helper(unsigned char *depth_map, int map_width, int x, int y);

int homogenous(unsigned char *depth_map, int map_width, int x, int y, int section_width) {

    unsigned char *pointer_H = (unsigned char *) malloc(sizeof(depth_map)/4 * sizeof(unsigned char));
    unsigned char *pointer_C = pointer_H;
    for(int i = 0; i < map_width * map_width; i++){
        unsigned char pixels = depth_map[i];
        int x1, y1 = 0;
        x1= i % map_width;
        y1 = i / map_width;
        
        if (x1 >= x && x1 < x + section_width && y1 >= y && y1 < y + section_width){
            *pointer_H = pixels;
            pointer_H = &pointer_H[1];
        }
    }
    int same = 1;
    int gray = *pointer_C;

    while(*pointer_C++){
        if (gray != *pointer_C){
            same = 0;
        }
    }
    return same; 
}
qNode *depth_to_quad(unsigned char *depth_map, int map_width) { 
    return depth_to_quad_helper(depth_map, map_width, 0, 0);
}

qNode *depth_to_quad_helper(unsigned char *depth_map, int map_width, int x, int y) {
    qNode *node = malloc(sizeof(qNode));
    if (node == NULL){
        allocation_failed();
    }
    int same = 1;
    unsigned char gray = *depth_map;
    for(int i = 0; i < map_width * map_width; i++){
        if (depth_map[i] != gray){
            same = 0;
        }
    }
    
    if (same){
        node->gray_value = gray;
        node->x = x;
        node->y = y;
        node->size = map_width;
        node->child_NW = NULL;
        node->child_NE = NULL;
        node->child_SE = NULL;
        node->child_SW = NULL;
        node->leaf = 1;
    }
    else{
        node->gray_value = 256;
        node->x = x;
        node->y = y;
        node->size = map_width;
        node->leaf = 0;
        node->child_NW = quad_helper_NW(depth_map, map_width, node->x, node->y);
        node->child_NE = quad_helper_NE(depth_map, map_width, node->x + map_width/2, node->y);
        node->child_SE = quad_helper_SE(depth_map, map_width, node->x + map_width/2, node->y + map_width/2);
        node->child_SW = quad_helper_SW(depth_map, map_width, node->x, node->y + map_width/2);
    }
    return node;
}

qNode* quad_helper_NW(unsigned char *depth_map, int map_width, int x, int y){

    qNode *node = malloc(sizeof(qNode));
    if (node == NULL){
        allocation_failed();
    }
    
    
    unsigned char *pointer_NW = (unsigned char *) malloc(sizeof(depth_map)/4 * sizeof(unsigned char));
    unsigned char *pointer_C = pointer_NW;

    for(int i = 0; i < map_width * map_width; i++){
        unsigned char pixels = depth_map[i];
        int x, y = 0;
        x = i % map_width;
        y = i / map_width;
        
        if (x < map_width/2 && y < map_width/2){
            *pointer_NW = pixels;
            pointer_NW = &pointer_NW[1];
        }
    }

    node = depth_to_quad_helper(pointer_C, map_width/2, x, y);
    return node;
}


qNode* quad_helper_NE(unsigned char *depth_map, int map_width, int x, int y){
    qNode *node = malloc(sizeof(qNode));
    if (node == NULL){
        allocation_failed();
    }
    unsigned char *pointer_NE = (unsigned char *) malloc(sizeof(depth_map)/4 * sizeof(unsigned char));
    unsigned char *pointer_C = pointer_NE;

    for(int i = 0; i < map_width * map_width; i++){
        unsigned char pixels = depth_map[i];
        int x, y = 0;
        x = i % map_width;
        y = i / map_width;
        
        if (x >= map_width/2 && y < map_width/2){
            *pointer_NE = pixels;
            pointer_NE = &pointer_NE[1];
        }
    }

    node = depth_to_quad_helper(pointer_C, map_width/2, x, y);
    return node;
}

qNode* quad_helper_SE(unsigned char *depth_map, int map_width, int x, int y){
    qNode *node = malloc(sizeof(qNode));
    if (node == NULL){
        allocation_failed();
    }
    unsigned char *pointer_SE = (unsigned char *) malloc(sizeof(depth_map)/4 * sizeof(unsigned char));
    unsigned char *pointer_C = pointer_SE;

    for(int i = 0; i < map_width * map_width; i++){
        unsigned char pixels = depth_map[i];
        int x, y = 0;
        x = i % map_width;
        y = i / map_width;
        
        if (x >= map_width/2 && y >= map_width/2){
            *pointer_SE = pixels;
            pointer_SE = &pointer_SE[1];
        }
    }

    node = depth_to_quad_helper(pointer_C, map_width/2, x, y);
    return node;
}

qNode* quad_helper_SW(unsigned char *depth_map, int map_width, int x, int y){
    qNode *node = malloc(sizeof(qNode));
    if (node == NULL){
        allocation_failed();
    }
    unsigned char *pointer_SW = (unsigned char *) malloc(sizeof(depth_map)/4 * sizeof(unsigned char));
    unsigned char *pointer_C = pointer_SW;

    for(int i = 0; i < map_width * map_width; i++){
        unsigned char pixels = depth_map[i];
        int x, y = 0;
        x = i % map_width;
        y = i / map_width;
        
        if (x < map_width/2 && y >= map_width/2){
            *pointer_SW = pixels;
            pointer_SW = &pointer_SW[1];
        }
    }

    node = depth_to_quad_helper(pointer_C, map_width/2, x, y);
    return node;
}

void free_qtree(qNode *qtree_node) {
    if(qtree_node) {
        if(!qtree_node->leaf){
            free_qtree(qtree_node->child_NW);
            free_qtree(qtree_node->child_NE);
            free_qtree(qtree_node->child_SE);
            free_qtree(qtree_node->child_SW);
        }
        free(qtree_node);
    }
}
