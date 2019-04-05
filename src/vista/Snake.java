package vista;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Snake extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7162722180024535879L;
	private ArrayList<Point> list;
	private Point sentido;
	private long espera;
	private int tam;
	private int celdas;
	private Point plus;
	private LocalDateTime inicio;
	private int nivel;
	private JFrame ventana;
	private int esperaIni;
	private int initSize;
	private double dificultad;
	private boolean boost = false;
	private int modo;

	public Snake(int dificultad, int cantCeldas, int tamCelda, int initSize, int modo, JFrame frame) {
		this.ventana = frame;
		list = new ArrayList<>();

		int size = initSize;
		while (size != 0)
			list.add(new Point((cantCeldas / 2) + size--, cantCeldas / 2));

		sentido = new Point(1, 0);

		this.initSize = initSize;
		this.dificultad = dificultad;
		this.espera = this.esperaIni = (int) (2000 / this.dificultad);
		this.tam = (cantCeldas) * (tamCelda);
		int deface = 2;
		this.ventana.setSize(this.tam + deface, this.tam + 23 + deface); // con 20x15 funciona

		this.celdas = cantCeldas;
		boost = false;
		this.modo = modo;
//		paintComponent(this.getGraphics());
	}

	private void nuevoPlus() {
		int x = (int) (Math.random() * (this.celdas - 1));
		int y = (int) (Math.random() * (this.celdas - 1));
		plus = new Point(x, y);
		nivel++;
		drawPlus();
	}

	private void drawPlus() {
		int x;
		int y;
		int tamCelda = tam / this.celdas;
		x = tamCelda * plus.x;
		y = tamCelda * plus.y;
		Graphics g = this.getGraphics();
		g.setColor(Color.RED);
		g.fillOval(x, y, tamCelda, tamCelda);
	}

	@SuppressWarnings("unchecked")
	private boolean mover() {
		Point temp = null;
		Point sentidotemp = sentido;
		ArrayList<Point> lista = new ArrayList<>();
		boolean primero = true;
		boolean comio = false;
		ArrayList<Point> listtemp = (ArrayList<Point>) list.clone();

		for (Point point : listtemp) {
			if (primero) {
				temp = new Point(point.x + sentidotemp.x, point.y + sentidotemp.y);
				if (perdio(temp))
					return false;
				if (comio = comePlus(temp))
					plus(lista);
				primero = false;
			}
			lista.add(temp);
			temp = point;
		}
		list = lista;
		if (comio)
			this.list.add(new Point(-1, -1));

		dibujar(this.getGraphics());
		return true;
	}

	private void plus(ArrayList<Point> list2) {
		// list2.add(ultimo);
		this.espera /= 1 + (0.04 * dificultad);
		nuevoPlus();
	}

	private boolean comePlus(Point temp) {
		if (temp.equals(plus))
			return true;
		return false;
	}

	private boolean perdio(Point temp) {
		if (list.contains(temp) && !temp.equals(list.get(list.size() - 1)))
			return true;
		if (temp.x == celdas - 1)
			return true;
		if (temp.y == celdas - 1)
			return true;
		if (temp.x == -1)
			return true;
		if (temp.y == -1)
			return true;
		return false;
	}

	@SuppressWarnings("unchecked")
	private void dibujar(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, tam, tam);
		int tamCelda = tam / celdas;
		ArrayList<Point> lista = (ArrayList<Point>) list.clone();
		boolean b = true;
		for (Point point : lista) {
			int x = tamCelda * point.x;
			int y = tamCelda * point.y;
			if (modo == 0) {
				Color hsbColor = Color.getHSBColor((float) (Math.random() * 200), (float) (Math.random() * 200),
						(float) (Math.random() * 200));
				g.setColor(hsbColor);
			} else {
				if (b) {
					g.setColor(Color.BLACK);
					b = false;
				} else {
					g.setColor(Color.GRAY);
					b = true;
				}
			}
			g.fillRect(x, y, tamCelda, tamCelda);
		}

		drawPlus();
		this.removeAll();
		this.repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		// super.paintComponent(g);
		dibujar(g);
	}

	public void jugar() {
		nuevoPlus();
		inicio = LocalDateTime.now();
		nivel = 0;
		new Thread() {
			public void run() {
				while (true) {
					LocalDateTime now = LocalDateTime.now();
					int min = now.getMinute() - inicio.getMinute() + 60;
					int sec = now.getSecond() - inicio.getSecond() + 60;
					String string = String.format("SNAKE3.0!  Nivel: %d Se jugo %02d:%02d", nivel, min % 60, sec % 60);
					ventana.setTitle(string);
					if (!mover()) {
						perder();
						return;
					}
					try {
						int div = 10;
						while (div-- != 0)
							if (boost)
								sleep(espera / 100);
							else
								sleep(espera / 10);
					} catch (Exception e) {
						System.out.println("Error en Wait");
					}
				}
			}
		}.start();
	}

	private void reinicio() {
		list = new ArrayList<>();

		int size = initSize;
		while (size != 0)
			list.add(new Point((celdas / 2) + size--, celdas / 2));

		sentido = new Point(1, 0);

		this.espera = this.esperaIni;
		boost = false;
		jugar();
	}

	private void perder() {
		// System.out.println("Perdiste!!! cabe por gil");
		String[] option = { "Volver a empezar", "Salgo por cagon" };
		int eligio = JOptionPane.showOptionDialog(null, "Perdiste!! cabe por gil", "Perdiste!!",
				JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, option, option[1]);

		if (eligio == 0)
			reinicio();
		else
			ventana.dispose();
	}

	public KeyAdapter cambioSentido() {
		return new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				switch (arg0.getKeyCode()) {
				case KeyEvent.VK_UP:
					if (sentido.x != 0)
						sentido = new Point(0, -1);
					break;
				case KeyEvent.VK_DOWN:
					if (sentido.x != 0)
						sentido = new Point(0, 1);
					break;
				case KeyEvent.VK_LEFT:
					if (sentido.y != 0)
						sentido = new Point(-1, 0);
					break;
				case KeyEvent.VK_RIGHT:
					if (sentido.y != 0)
						sentido = new Point(1, 0);
					break;
				case KeyEvent.VK_SPACE:
					boost = true;
					break;
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_SPACE)
					boost = false;
			}
		};
	}
}
