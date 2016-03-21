#!/usr/bin/env python

from random import sample,randint as rand
import matplotlib.pyplot as pyplot

def pickNeighbor(x,y,w,h):
    neighbors = []
    if x > 1:      neighbors.append((y, x - 2))
    if x < w - 2:  neighbors.append((y, x + 2))
    if y > 1:      neighbors.append((y - 2, x))
    if y < h - 2:  neighbors.append((y + 2, x))
    if not neighbors: return None,None
    return sample(neighbors,1)[0]
def maze(w=45, h=45, complx=.75, dens=.75):
    # Make sides ODDS
    h,w = (h//2)*2 + 1, (w//2)*2 + 1
    # Adjust complx and dens relative to maze size
    complx = int( complx * (5 * (h+w))  )
    dens = int( dens * ((h//2) * (w//2)) )
    # Maze Array
    Z = [ [ False for j in range(w)] for i in range(h) ]
    # Enclose Borders
    for i in range(w): Z[0][i] = Z[-1][i] = True
    for i in range(h): Z[i][0] = Z[i][-1] = True
    # Make aisles
    for i in range(dens):
        x, y = rand(0, w//2) * 2, rand(0, h//2) * 2
        Z[y][x] = True #Random Wall
        for j in range(complx):
            y_,x_ = pickNeighbor(x,y,w,h)
            if y_ is not None and x_ is not None and Z[y_][x_] == 0:
                Z[y_][x_] = Z[y_ + (y-y_)//2][x_ + (x-x_)//2] = True
                x, y = x_, y_
    for i in sample(range(4),2):
        if i==0: Z[rand(1,h-1)][0] = False 
        elif i==1: Z[rand(1,h-1)][-1] = False 
        elif i==2: Z[0][rand(1,w-1)] = False
        elif i==3: Z[-1][rand(1,w-1)] = False
    return Z

pyplot.figure(figsize=(10, 5))
pyplot.imshow(maze(), cmap=pyplot.cm.binary,  interpolation='nearest')
pyplot.xticks([]), pyplot.yticks([])
pyplot.show()