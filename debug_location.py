
# This is a simple program to make sure that locations.txt is done correctly
# it basically visualizes the contents of locations.txt

import Tkinter as tk
import sys
from PIL import Image, ImageTk, ImageDraw



locations = []
passive_locations = []


def parse_locations():
  textf = open("locations.txt","r")
  lines = textf.readlines()

  for line in lines:
    line = line.strip()
    if len(line) == 0 or line[0] == '#':
      continue
    lineb = line.split(" ")
    if lineb[0] == "location":
      locations.append((lineb[1],int(lineb[2]),int(lineb[3])))
    elif lineb[0] == "passive_location":
      passive_locations.append((lineb[1],int(lineb[2]),int(lineb[3])))
    else:
      print "Unrecognized: ", line
      sys.exit(0)

parse_locations()
#print locations




"""
def graphics_small():

# Width of smaller image
  IM_WIDTH = 1200.0
  IM_HEIGHT = 913.0

# Width of bigger image (our coordinates are with respect to this)
  original_width = 3175.0
  original_ratio = IM_WIDTH / original_width

  root = tk.Tk()
  map_im_ = Image.open("map_1200.png")
  map_im_.thumbnail((1200,1200),Image.ANTIALIAS)


  draw = ImageDraw.Draw(map_im_)

  for loc in locations:
    coord = (loc[1]*original_ratio, loc[2]*original_ratio)
    rad = 8

    draw.ellipse((coord[0]-rad,coord[1]-rad,coord[0]+rad,coord[1]+rad),fill="blue")

  for loc in passive_locations:
    coord = (loc[1]*original_ratio, loc[2]*original_ratio)
    rad = 4

    draw.ellipse((coord[0]-rad,coord[1]-rad,coord[0]+rad,coord[1]+rad),fill="black")


  map_im = ImageTk.PhotoImage(map_im_)

  panel1 = tk.Label(root, image=map_im)
  panel1.pack(side='left', fill='both', expand='yes')

  root.mainloop()

"""


# converts original pixel to displayed (large)
def conv(x,y):
  original_ratio = 0.755905512
  offset_x = 1195
  offset_y = 875

  return ((x-offset_x)*original_ratio,(y-offset_y)*original_ratio)


def graphics_large():


  root = tk.Tk()
  map_im_ = Image.open("map_2400.png")
  map_im_.thumbnail((1496,672),Image.ANTIALIAS)


  draw = ImageDraw.Draw(map_im_)

  for loc in locations:
    coord = conv(loc[1],loc[2])
    rad = 8

    draw.ellipse((coord[0]-rad,coord[1]-rad,coord[0]+rad,coord[1]+rad),fill="blue")

  for loc in passive_locations:
    coord = conv(loc[1],loc[2])
    rad = 4

    draw.ellipse((coord[0]-rad,coord[1]-rad,coord[0]+rad,coord[1]+rad),fill="red")


  map_im = ImageTk.PhotoImage(map_im_)

  panel1 = tk.Label(root, image=map_im)
  panel1.pack(side='left', fill='both', expand='yes')

  root.mainloop()


graphics_large()


