import Tkinter as tk
from PIL import Image, ImageTk, ImageDraw

class GraphicalEditor:
  """Graphical map editor. Renders map data visually using Tkinter."""

  def __init__(self, map_data, map_file):
    root = tk.Tk()
    map_im_ = Image.open(map_file)
    map_im_.thumbnail((1496,801),Image.ANTIALIAS)

    draw = ImageDraw.Draw(map_im_)

    for loc in map_data.locations:
      coord = GraphicalEditor._conv(loc[1],loc[2])
      rad = 6

      draw.ellipse((coord[0]-rad,coord[1]-rad,coord[0]+rad,coord[1]+rad),fill="blue")

    for loc in map_data.passive_locations:
      coord = GraphicalEditor._conv(loc[1],loc[2])
      rad = 4

      draw.ellipse((coord[0]-rad,coord[1]-rad,coord[0]+rad,coord[1]+rad),fill="red")
      draw.text((coord[0],coord[1]),loc[0],(0,0,0))


    for path in map_data.paths:
      coord1 = GraphicalEditor._conv(path[0][0],path[0][1])
      coord2 = GraphicalEditor._conv(path[1][0],path[1][1])
      
      draw.line((coord1[0],coord1[1],coord2[0],coord2[1]),fill="black")


    map_im = ImageTk.PhotoImage(map_im_)

    panel1 = tk.Label(root, image=map_im)
    panel1.pack(side='left', fill='both', expand='yes')

    root.bind("<1>", self._mousedown)
    root.mainloop()

  def _mousedown(self, event):
    """Handle mouse click"""
    # Print map coordinates of location clicked
    (cx,cy) = GraphicalEditor._conv2(event.x,event.y)
    print "  p",int(cx),int(cy)

  @staticmethod
  def _conv(x, y):
    """converts original pixel to displayed (large)"""
    original_ratio = 0.755905512
    offset_x = 1195
    offset_y = 872
    return ((x-offset_x)*original_ratio,(y-offset_y)*original_ratio)

  @staticmethod
  def _conv2(x, y):
    """opposite of conv"""
    original_ratio = 0.755905512
    offset_x = 1195
    offset_y = 872
    return (x/original_ratio + offset_x, y/original_ratio + offset_y)
