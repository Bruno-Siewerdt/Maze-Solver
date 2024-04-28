package Simulation;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Dots {
	
	public static final int TILE_SIZE = 32;
	double x, y;
	public boolean down, right;
	static int MAX_L = 20;
	static int MAX_C = 32;
	
	public Dots(double x, double y, boolean down, boolean right){
		this.x = x;
		this.y = y;
		this.down = down;
		this.right = right;
	}
	
	public int getX() {
		return (int) x;
	}
	
	public int getY() {
		return (int) y;
	}
	
	public static ArrayList<Dots> create_DOTS(){
		ArrayList<Dots> DOTS;
		DOTS = new ArrayList<Dots>();
		
		for(int l = 0; l < MAX_L; l++) {
			for(int c = 0; c < MAX_C; c++) {
				boolean d = false, r = false;
				if((l == 0 || l == MAX_L-1) && c != Math.floor(MAX_C/2 - 1) && c != MAX_C-1) {
					r = true;
				}
				if((c == 0 || c == MAX_C-1) && l != MAX_L-1) {
					d = true;
				}
				Dots dot = new Dots((c+3.2)*TILE_SIZE, (l+3)*TILE_SIZE, d, r);
				DOTS.add(dot);
			}
		}
		return DOTS;
	}
	
	public static void make_wall(Dots d1, Dots d2) {
		if(d1.getY() == d2.getY() && d1.getX() != d2.getX()) {
			int l = (int)(d1.getY()/TILE_SIZE - 3);
			if(l > 0 && l < MAX_L-1) {
				for(int c = (int) (Math.min(d1.getX(), d2.getX())/TILE_SIZE - 2.2); c < Math.max(d1.getX(), d2.getX())/TILE_SIZE - 3.2; c++) {
					Sim.DOTS.get(c + l*TILE_SIZE).right = true;
				}
			}
		}
		if(d1.getY() != d2.getY() && d1.getX() == d2.getX()) {
			int c = (int)(d1.getX()/TILE_SIZE - 2.2);
			if(c > 0 && c < MAX_C-1) {
				for(int l = (int)(Math.min(d1.getY(), d2.getY())/TILE_SIZE - 3); l < Math.max(d1.getY(), d2.getY())/TILE_SIZE - 3; l++) {
					Sim.DOTS.get(c + l*TILE_SIZE).down = true;
				}
			}
		}
	}
	
	public static void delete_wall(Dots d1, Dots d2) {
		if(d1.getY() == d2.getY() && d1.getX() != d2.getX()) {
			int l = (int)(d1.getY()/TILE_SIZE - 3);
			if(l > 0 && l < MAX_L-1) {
				for(int c = (int) (Math.min(d1.getX(), d2.getX())/TILE_SIZE - 2.2); c < Math.max(d1.getX(), d2.getX())/TILE_SIZE - 3.2; c++) {
					Sim.DOTS.get(c + l*TILE_SIZE).right = false;
				}
			}
		}
		if(d1.getY() != d2.getY() && d1.getX() == d2.getX()) {
			int c = (int)(d1.getX()/TILE_SIZE - 2.2);
			if(c > 0 && c < MAX_C-1) {
				for(int l = (int)(Math.min(d1.getY(), d2.getY())/TILE_SIZE - 3); l < Math.max(d1.getY(), d2.getY())/TILE_SIZE - 3; l++) {
					Sim.DOTS.get(c + l*TILE_SIZE).down = false;
				}
			}
		}
	}
	
	public static boolean hit_wall(Dots d1, Dots d2) {
		if(d1.getY() == d2.getY()) {
			int l = (int) Math.floor(d1.getY()/TILE_SIZE - 2.5);
			int c = (int)(((d1.getX()/TILE_SIZE - 2.2) + (d2.getX()/TILE_SIZE - 2.2))/2);
			return Sim.DOTS.get(c + l*TILE_SIZE).down;
		} else {
			int c = (int) Math.floor(d1.getX()/TILE_SIZE - 2.2);
			int l = (int)(((d1.getY()/TILE_SIZE - 2.5) + (d2.getY()/TILE_SIZE - 2.5))/2);
			return Sim.DOTS.get(c + l*TILE_SIZE).right;
		}
	}
	
	static boolean isOnList(Dots d, ArrayList<Dots> dots) {
		boolean onList = false;
		for(int i = 0; i < dots.size(); i++) {
			if(dots.get(i).y == d.y && dots.get(i).x == d.x) {
				onList = true;
				break;
			}
		}
		return onList;
	}
	
	public static Dot_solve solve_maze(Dots curr_dot, ArrayList<Dots> prev_dots){
		Dot_solve d_solve = new Dot_solve(null, false);
		if(curr_dot.y >= (MAX_L+2)*TILE_SIZE) {
			ArrayList<Dots> dots = new ArrayList<Dots>();
			dots.add(curr_dot);
			return new Dot_solve(dots, true);
		}
		
		if(!hit_wall(curr_dot, new Dots(curr_dot.x, curr_dot.y + TILE_SIZE, false, false)) && !isOnList(new Dots(curr_dot.x, curr_dot.y + TILE_SIZE, false, false), prev_dots)) {
			prev_dots.add(curr_dot);
			d_solve = solve_maze(new Dots(curr_dot.x, curr_dot.y + TILE_SIZE, false, false), prev_dots);
		}
		if(d_solve.solved) {
			ArrayList<Dots> dots = new ArrayList<Dots>();
			dots = d_solve.DOTS;
			dots.add(curr_dot);
			return new Dot_solve(dots, true);
		}
		
		if(!d_solve.solved && !hit_wall(curr_dot, new Dots(curr_dot.x + TILE_SIZE, curr_dot.y, false, false)) && !isOnList(new Dots(curr_dot.x + TILE_SIZE, curr_dot.y, false, false), prev_dots)) {
			prev_dots.add(curr_dot);
			d_solve = solve_maze(new Dots(curr_dot.x + TILE_SIZE, curr_dot.y, false, false), prev_dots);
		}
		if(d_solve.solved) {
			ArrayList<Dots> dots = new ArrayList<Dots>();
			dots = d_solve.DOTS;
			dots.add(curr_dot);
			return new Dot_solve(dots, true);
		}
		
		if(!d_solve.solved && !hit_wall(new Dots(curr_dot.x - TILE_SIZE, curr_dot.y, false, false), curr_dot) && !isOnList(new Dots(curr_dot.x - TILE_SIZE, curr_dot.y, false, false), prev_dots)) {
			prev_dots.add(curr_dot);
			d_solve = solve_maze(new Dots(curr_dot.x - TILE_SIZE, curr_dot.y, false, false), prev_dots);
		}
		if(d_solve.solved) {
			ArrayList<Dots> dots = new ArrayList<Dots>();
			dots = d_solve.DOTS;
			dots.add(curr_dot);
			return new Dot_solve(dots, true);
		}
		
		if(!d_solve.solved && !hit_wall(new Dots(curr_dot.x, curr_dot.y - TILE_SIZE, false, false), curr_dot) && !isOnList(new Dots(curr_dot.x, curr_dot.y - TILE_SIZE, false, false), prev_dots)) {
			prev_dots.add(curr_dot);
			d_solve = solve_maze(new Dots(curr_dot.x, curr_dot.y - TILE_SIZE, false, false), prev_dots);
		}
		if(d_solve.solved) {
			ArrayList<Dots> dots = new ArrayList<Dots>();
			dots = d_solve.DOTS;
			dots.add(curr_dot);
			return new Dot_solve(dots, true);
		}
		return new Dot_solve(null, false);
	}
	
	public void render(Graphics g) {
		g.setColor(Color.WHITE);
		if(this.down) {
			g.fillRect(this.getX(), this.getY(), 1, TILE_SIZE);
		}
		if(this.right) {
			g.fillRect(this.getX(), this.getY(), TILE_SIZE, 1);
		}
		if(Sim.MODE == "Setting") {
			g.fillRect(this.getX(), this.getY(), 1, 1);
			if(Math.abs(this.getX() - Sim.MOUSE_X) < 10 && Math.abs(this.getY() - Sim.MOUSE_Y) < 10) {
				g.setColor(Color.RED);
				g.drawRoundRect(this.getX()-3, this.getY()-4, 7, 7, 7, 7);
			}
		}
	}
		
}
