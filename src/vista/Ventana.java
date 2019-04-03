package vista;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Ventana {

	private JFrame frame;
	private static Snake snake;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Ventana window = new Ventana();
					window.frame.setVisible(true);
					snake.jugar();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Ventana() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		frame.getContentPane().setLayout(gridBagLayout);
		try {
			String respuesta = JOptionPane.showInputDialog(null, "Ingrese la cantidad de celdas");
			int cant = Integer.parseInt(respuesta);
			// respuesta = JOptionPane.showInputDialog(null, "Ingrese el tamaño de las
			// celdas (Recomendado 15)");
			// int size = Integer.parseInt(respuesta);
			respuesta = JOptionPane.showInputDialog(null, "Ingrese el nivel de dificultad");
			int dif = Integer.parseInt(respuesta);

			String[] option = { "Modo Epileptico", "Modo cagon" };
			int modo = JOptionPane.showOptionDialog(null, "Elegi un modo wacho", "Que miras el titulo?",
					JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, option, option[1]);
			snake = new Snake(dif, cant, 13, (int) (cant / 7) * 2, modo, this.frame);
		} catch (Exception e) {
			frame.dispose();
		}
		frame.addKeyListener(snake.cambioSentido());
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		frame.getContentPane().add(snake, gbc_panel);
	}

}
