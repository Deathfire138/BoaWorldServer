package model;

import java.util.Deque;
import java.util.LinkedList;

import net.ActionSender;

import miscellaneous.Utils;

public class WalkingQueue {
	
	private static final byte[] DIRECTION_DELTA_X = new byte[] {-1, 0, 1, -1, 1, -1, 0, 1};

	private static final byte[] DIRECTION_DELTA_Y = new byte[] {1, 1, 1, 0, 0, -1, -1, -1};

	private static class Point {

		private final int x;

		private final int y;

		private final int dir;

		public Point(int x, int y, int dir) {
			this.x = x;
			this.y = y;
			this.dir = dir;
		}

	}

	private static final int MAXIMUM_SIZE = 50;
	
	private static final int NORTH = 1, SOUTH = 6, EAST = 4, WEST = 3, NORTH_EAST = 2, SOUTH_EAST = 7, NORTH_WEST = 0, SOUTH_WEST = 5;

	private final Entity entity;

	private final Deque<Point> waypoints = new LinkedList<Point>();

	private boolean runToggled = false;

	private boolean runQueue = false;

	public WalkingQueue(Entity entity) {
		this.entity = entity;
	}

	public void setRunningToggled() {
		this.runToggled = !runToggled;
	}

	public void setRunningQueue(boolean runQueue) {
		this.runQueue = runQueue;
	}

	public boolean isRunningToggled() {
		return runToggled;
	}

	public boolean isRunningQueue() {
		return runQueue;
	}

	public boolean isRunning() {
		return runToggled || runQueue;
	}

	public void reset() {
		runQueue = false;
		waypoints.clear();
		waypoints.add(new Point(entity.getLocation().getX(), entity.getLocation().getY(), -1));
	}

	public boolean isEmpty() {
		return waypoints.isEmpty();
	}

	public void finish() {
		waypoints.removeFirst();
	}
	
    public void walkingTo(int x, int y) {
	    reset();
	    addStep(x, y);
	    finish();
    }
    
    public void walkingTo(int[] x, int[] y) {
	    reset();
	    for (int i = 0; i < x.length; i++) {
	    	addStep(x[i], y[i]);
	    }
	    finish();
    }

	public void addStep(int x, int y) {
		if (waypoints.size() == 0) {
			reset();
		}
		final Point last = waypoints.peekLast();
		int diffX = x - last.x;
		int diffY = y - last.y;
		final int max = Math.max(Math.abs(diffX), Math.abs(diffY));
		for (int i = 0; i < max; i++) {
			if (diffX < 0) {
				diffX++;
			} else if (diffX > 0) {
				diffX--;
			}
			if (diffY < 0) {
				diffY++;
			} else if (diffY > 0) {
				diffY--;
			}
			addStepInternal(x - diffX, y - diffY);
		}
	}

	public void addStepInternal(int x, int y) {
		if (waypoints.size() >= MAXIMUM_SIZE) {
			return;
		}
		final Point last = waypoints.peekLast();
		final int diffX = x - last.x;
		final int diffY = y - last.y;
		final int dir = Utils.direction(diffX, diffY);
		if (dir > -1) {
			waypoints.add(new Point(x, y, dir));
		}
	}
	
	public void addStepInternal(int x, int y, int dir) {
		if (waypoints.size() >= MAXIMUM_SIZE) {
            return;
        }
        if (dir != -1) {
        	waypoints.add(new Point(x, y, dir));
        }
    }

	public void processNextMovement() {
		/*if (entity instanceof Npc) {
			int range = ((Integer) entity.getTemporary("RANGE"));
			if (range != 0 && entity.getTemporary("UNMOVABLE") ==  null && entity.getTemporary("TELEPORT_TARGET") == null  && entity.getTemporary("DEAD") == null && entity.getTemporary("COMBAT_INSTANCE") == null && Math.random() > 0.8) {
				Location locaction = (Location)entity.getTemporary("STARTING_LOCATION");
				int minX = locaction.getX() - range, minY = locaction.getY() - range, maxX = locaction.getX() + range, maxY = locaction.getY() + range;
				int moveX = (int) (Math.floor((Math.random() * 3)) - 1), moveY = (int) (Math.floor((Math.random() * 3)) - 1);
				int tgtX = entity.getLocation().getX() + moveX, tgtY = entity.getLocation().getY() + moveY;
				boolean canMove = true;
				if (!Pathfinder.canMove(entity, tgtX, tgtY, 1, 1)) {
					canMove = false;
				}
				if(canMove && !(tgtX > maxX || tgtX < minX || tgtY > maxY || tgtY < minY)) {
					walkingTo(tgtX, tgtY);
				}
			}
		}*/
		try {
		Point walkPoint = null, runPoint = null;
		final Location target = (Location) entity.getTemporary("TELEPORT_TARGET");
		if (target != null) {
			System.out.println("teleport target != null");
			reset();
			entity.setLocation(target);
			entity.addTemporary("HAS_TELEPORTED", true);
		} else {
			walkPoint = getNextPoint();
			if (runToggled || runQueue) {
				boolean canRun = false;
				if (entity instanceof Player) {
					canRun = ((Player) entity).getSettings().getEnergy() > 0;
				}
				if (canRun) {
					runPoint = getNextPoint();
				} else {
					if (entity instanceof Player) {
						runToggled = runQueue = false;
						ActionSender.sendConfig(((Player) entity), 173, 0);
					}
				}
			}
			if (runPoint != null) {
				if (entity instanceof Player) {
					((Player) entity).getSettings().setEnergy(((Player) entity).getSettings().getEnergy() - 0.88);
				} else {
					if (isRunningToggled()) {
						runToggled = runQueue = false;
					}
				}
			}
			final int walkDir = walkPoint == null ? -1 : walkPoint.dir;
			final int runDir = runPoint == null ? -1 : runPoint.dir;
			entity.getSprites().setSprites(walkDir, runDir);
			if (walkPoint == null && runPoint == null && entity.getTemporary("IS_WALKING") != null) {
				entity.removeTemporary("IS_WALKING");
			} else if (entity.getTemporary("IS_WALKING") == null) {
				entity.addTemporary("IS_WALKING", true);
			}
		}
		if (entity instanceof Player) {
			Location lastRegion = (Location) entity.getTemporary("LAST_KNOWN_REGION");
			if (lastRegion != null) {
				int diffX = entity.getLocation().getX() - (lastRegion.getRegionX() - 6) * 8;
				int diffY = entity.getLocation().getY() - (lastRegion.getRegionY() - 6) * 8;
				boolean changed = false;
				if(diffX < 16) {
					changed = true;
				} else if(diffX >= 88) {
					changed = true;
				}
				if(diffY < 16) {
					changed = true;
				} else if(diffY >= 88) {
					changed = true;
				}
				if(changed) {
					System.out.println("change map");
					entity.addTemporary("MAP_REGION_CHANGING", true);
				}
			} else {
				System.out.println("change map");
				entity.addTemporary("MAP_REGION_CHANGING", true);
			}
		}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private Point getNextPoint() {
		if (waypoints.size() == 0) {
			return null;
		}
		Point p = waypoints.poll();
		if (p == null || p.dir == -1) {
			return null;
		} else {
			if (p.dir == -1) {
				entity.setLocation(new Location(p.x, p.y, 0));
			} else {
				int diffX = DIRECTION_DELTA_X[p.dir];
				int diffY = DIRECTION_DELTA_Y[p.dir];
				int direction = -1;
				
				if(diffX != 0) {
					if(diffX > 0) {
						if(diffY != 0) {
							if(diffY > 0) {
								direction = NORTH_EAST;
							} else {
								direction = SOUTH_EAST;
							}
						} else {
							direction = EAST;
						}
					} else {
						if(diffY != 0) {
							if(diffY > 0) {
								direction = NORTH_WEST;
							} else {
								direction = SOUTH_WEST;
							}
						} else {
							direction = WEST;
						}
					}
				}
				if(direction == -1) {
					if(diffY != 0) {
						if(diffY > 0) {
							direction = NORTH;
						} else {
							direction = SOUTH;
						}
					}
				}
				entity.addTemporary("FACING", direction);
				entity.setLocation(entity.getLocation().transform(diffX, diffY, 0));
			}
			return p;
		}
	}
}