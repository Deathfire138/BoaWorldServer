package packet.handlers;

import boa.event.InterfaceEvent;
import model.Player;
import packet.Packet;
import packet.PacketHandler;

public class WalkingPackets extends PacketHandler {

	@Override
	public void handle(Player player, Packet packet) {
		if (player.getTemporary("INTERFACE_EVENT") != null) {
			((InterfaceEvent) (player.getTemporary("INTERFACE_EVENT"))).close();
		}
		player.getWalkingQueue().reset();
		int size = packet.getSize();
		//System.out.println("size = "+size);
		//int size2 = packet.get();
		if (packet.getId() == 74) {
			size -= 14;
		}
		final int firstY = packet.getLEShortA();
		final int firstX = packet.getLEShort();
		//System.out.println("firstX = "+firstX);
		//System.out.println("firstY = "+firstY);
		//final boolean runSteps = packet.getS() == 1;
		int run = packet.getA();
		System.out.println(run);
		final boolean runSteps = run == -1;
		
		//System.out.println("runSteps = "+runSteps);
		final int steps = (size - 5) / 2;
		final int[][] path = new int[steps][2];
		for (int i = 0; i < steps; i++) {
			path[i][0] = packet.getA();
			//System.out.println("xstep = "+path[i][0]);
			path[i][1] = packet.getA();
			//System.out.println("ystep = "+path[i][1]);
		}
		player.getWalkingQueue().setRunningQueue(runSteps);
		player.getWalkingQueue().addStep(firstX, firstY);
		for (int i = 0; i < steps; i++) {
			path[i][0] += firstX;
			path[i][1] += firstY;
			player.getWalkingQueue().addStep(path[i][0], path[i][1]);
		}
		player.getWalkingQueue().finish();
	}

	@Override
	public int[] getPacketOpcodes() {
		return new int[] {74, 149, 177};
	}
}