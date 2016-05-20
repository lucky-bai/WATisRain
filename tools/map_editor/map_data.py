class MapData:
  def __init__(self, locations, passive_locations, paths, name_to_coord):

    # [("name",x,y)]
    self.locations = locations
    self.passive_locations = passive_locations

    # [(x,y)]
    self.paths = paths

    # ["name":(x,y)]
    self.name_to_coord = name_to_coord
