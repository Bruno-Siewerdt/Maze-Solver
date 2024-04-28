package Simulation;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;

public class Sim extends Canvas implements Runnable, MouseListener, MouseMotionListener, KeyListener{
	
	private static final long serialVersionUID = 1L;
	public static int WIDTH = 1200;
	public static int HEIGHT = 800;
	public static ArrayList<Dots> DOTS, DOTS_SOLVED;
	public static int MOUSE_X;
	public static int MOUSE_Y;
	public static String MODE = "Setting";
	Dots Selected_dot = null, Selected_dot_2 = null;
	
	public BufferedImage layer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	
	public Sim() {
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
	}
	
	public static void main(String[] args) {
		Sim game = new Sim();
		JFrame frame = new JFrame("Maze");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(game);
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		DOTS = Dots.create_DOTS();
		DOTS_SOLVED = new ArrayList<Dots>();
		new Thread(game).start();
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs==null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = layer.getGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		for(int i = 0; i < DOTS.size(); i++) {
			DOTS.get(i).render(g);
		}
		
		if(MODE == "Setting") {
			if(Selected_dot != null) {
				g.setColor(Color.WHITE);
				g.drawLine(MOUSE_X, MOUSE_Y, Selected_dot.getX(), Selected_dot.getY());                
			}
			if(Selected_dot_2 != null) {
				g.setColor(Color.RED);
				g.drawLine(MOUSE_X, MOUSE_Y, Selected_dot_2.getX(), Selected_dot_2.getY());                
			}
		} else if(MODE == "Simulating") {
			g.setColor(Color.BLUE);
			for(int i = 1; i < DOTS_SOLVED.size(); i++) {
				g.drawLine(DOTS_SOLVED.get(i-1).getX(), DOTS_SOLVED.get(i-1).getY(), DOTS_SOLVED.get(i).getX(), DOTS_SOLVED.get(i).getY());
			}
		}
		
		
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(layer, 0, 0, WIDTH, HEIGHT, null);
		bs.show();
	}
	
	@Override
	public void run() {
		requestFocus();
		while(true) {
			render();
			try {
				Thread.sleep(1000/60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		MOUSE_X = e.getX();
		MOUSE_Y = e.getY();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(MODE == "Setting") {
			if(e.getButton() == 3) {
				if(Selected_dot != null) {
					Selected_dot = null;
				} else {
					if(Selected_dot_2 == null) {
						for(int i = 0; i < DOTS.size(); i++) {
							Dots d = DOTS.get(i);
							if(Math.abs(d.getX() - e.getX()) < 10 && Math.abs(d.getY() - e.getY()) < 10) {
								Selected_dot_2 = d;
								break;
							}
						}
					} else {
						boolean found = false;
						for(int i = 0; i < DOTS.size(); i++) {
							Dots d = DOTS.get(i);
							if(Math.abs(d.getX() - e.getX()) < 10 && Math.abs(d.getY() - e.getY()) < 10) {
								Dots.delete_wall(Selected_dot_2, d);
								Selected_dot_2 = d;
								found = true;
								break;
							}
						}
						if(!found) {
							Selected_dot_2 = null;
						}
					}
				}
				
			} else if(e.getButton() == 1) {
				if(Selected_dot_2 != null) {
					Selected_dot_2 = null;
				} else {
					if(Selected_dot == null) {
						for(int i = 0; i < DOTS.size(); i++) {
							Dots d = DOTS.get(i);
							if(Math.abs(d.getX() - e.getX()) < 10 && Math.abs(d.getY() - e.getY()) < 10) {
								Selected_dot = d;
								break;
							}
						}
					} else {
						boolean found = false;
						for(int i = 0; i < DOTS.size(); i++) {
							Dots d = DOTS.get(i);
							if(Math.abs(d.getX() - e.getX()) < 10 && Math.abs(d.getY() - e.getY()) < 10) {
								Dots.make_wall(Selected_dot, d);
								Selected_dot = d;
								found = true;
								break;
							}
						}
						if(!found) {
							Selected_dot = null;
						}
					}
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_P) {
			if(MODE == "Setting") {
				MODE = "Simulating";
				ArrayList<Dots> dots = new ArrayList<Dots>();
				dots.add(new Dots(WIDTH/2, 2.5*Dots.TILE_SIZE - Dots.TILE_SIZE, false, false));
				DOTS_SOLVED = Dots.solve_maze(new Dots(WIDTH/2, 2.5*Dots.TILE_SIZE, false, false), dots).DOTS;
			} else if(MODE == "Simulating") {
				MODE = "Setting";
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_R) {
			if(MODE == "Setting") {
				DOTS = Dots.create_DOTS();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}
}