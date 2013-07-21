package model;

import model.Damage.HitType;

public class Npc extends Entity {



	public Npc(Location location) {
		super(location);
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return -1;
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changeRegion(Location to, Location from) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getClientIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void turnTo(Entity entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void turnTemporarilyTo(Entity entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playGraphics(Graphic graphic) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playAnimation(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void heal(int amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hit(int damage, HitType type) {
		// TODO Auto-generated method stub
		
	}
	
}
