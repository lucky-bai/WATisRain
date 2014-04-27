
# This is a simple program to make sure that locations.txt is done correctly
# it basically visualizes the contents of locations.txt

import Tkinter as tk
import sys
from PIL import Image, ImageTk, ImageDraw



# Parsing the map file

# [("name",x,y)]
locations = []
passive_locations = []

# [(x,y)]
paths = []

# ["name":(x,y)]
name_to_coord = dict()


def parse_locations():
  textf = open("locations.txt","r")
  lines = textf.readlines()

  for line in lines:
    line = line.strip()
    if len(line) == 0 or line[0] == '#':
      continue
    lineb = line.split(" ")
    command = lineb[0]

    if command == "location":
      locations.append((lineb[1],int(lineb[2]),int(lineb[3])))
      name_to_coord[lineb[1]] = (int(lineb[2]),int(lineb[3]))

    elif command == "passive_location":
      passive_locations.append((lineb[1],int(lineb[2]),int(lineb[3])))
      name_to_coord[lineb[1]] = (int(lineb[2]),int(lineb[3]))

    elif command == "path":
      path1 = lineb[1]
      path2 = lineb[2]
      # slowly phasing out this tool - don't show if there's no semicolon
      #if len(lineb) <= 3:
        #continue
      paths.append((name_to_coord[path1],name_to_coord[path2]))

    """
    else:
      print "Unrecognized: ", line
      sys.exit(0)
      """

parse_locations()



# converts original pixel to displayed (large)
def conv(x,y):
  original_ratio = 0.755905512
  offset_x = 1195
  offset_y = 872

  return ((x-offset_x)*original_ratio,(y-offset_y)*original_ratio)


# opposite of conv
def conv2(x,y):
  original_ratio = 0.755905512
  offset_x = 1195
  offset_y = 872

  return (x/original_ratio + offset_x, y/original_ratio + offset_y)
  


def graphics_large():


  root = tk.Tk()
  map_im_ = Image.open("map_2400.png")
  map_im_.thumbnail((1496,801),Image.ANTIALIAS)


  draw = ImageDraw.Draw(map_im_)

  for loc in locations:
    coord = conv(loc[1],loc[2])
    rad = 6

    draw.ellipse((coord[0]-rad,coord[1]-rad,coord[0]+rad,coord[1]+rad),fill="blue")

  for loc in passive_locations:
    coord = conv(loc[1],loc[2])
    rad = 4

    draw.ellipse((coord[0]-rad,coord[1]-rad,coord[0]+rad,coord[1]+rad),fill="red")
    draw.text((coord[0],coord[1]),loc[0],(0,0,0))


  for path in paths:
    coord1 = conv(path[0][0],path[0][1])
    coord2 = conv(path[1][0],path[1][1])
    
    draw.line((coord1[0],coord1[1],coord2[0],coord2[1]),fill="black")


  map_im = ImageTk.PhotoImage(map_im_)

  panel1 = tk.Label(root, image=map_im)
  panel1.pack(side='left', fill='both', expand='yes')

  # handle mouse click
  def mousedown(event):
    (cx,cy) = conv2(event.x,event.y)
    print "  p",int(cx),int(cy)

  root.bind("<1>", mousedown)

  root.mainloop()



def statistics():
  all_loc = locations + passive_locations

  for place in all_loc:
    (place_name,px,py) = place

    # count
    count = 0
    for p in paths:
      if p[0] == (px,py) or p[1] == (px,py):
        count += 1

    print place_name, count


graphics_large()
#statistics()


