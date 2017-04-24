package clzola.lightsout;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;

import com.alee.laf.WebLookAndFeel;

public class LightsOut extends JFrame {
	private static final long serialVersionUID = -3434472030023267006L;
	
	protected JPanel m_MainPanel;
	protected JPanel m_Menu;
	
	protected JSpinner m_CanvasSize;
	protected JButton  m_NewGameButton;
	protected JButton  m_RestartButton;
	protected JButton  m_SolutionButton;
	
	protected Model       m_Model;
	protected ModelCanvas m_ModelCanvas;
	
	public LightsOut() {
		setTitle("Lights Out");
		setResizable(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		m_Menu = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
		
		m_CanvasSize = new JSpinner();
		m_CanvasSize.setModel(new SpinnerListModel(new String[] {"3 x 3", "4 x 4", "5 x 5", "6 x 6", "7 x 7", "8 x 8", "9 x 9", "10 x 10"}));
		((JSpinner.DefaultEditor)m_CanvasSize.getEditor()).getTextField().setEditable(false);
		
		m_NewGameButton = new JButton("new game");
		m_NewGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				
			}
		});
		
		m_RestartButton = new JButton("restart");
		m_RestartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				
			}
		});
		
		m_SolutionButton = new JButton("solution");
		m_SolutionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				
			}
		});
		
		m_Menu.add(m_CanvasSize);
		m_Menu.add(m_NewGameButton);
		m_Menu.add(m_RestartButton);
		m_Menu.add(m_SolutionButton);
		
		m_NewGameButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				String size = (String) m_CanvasSize.getValue();
				Point xy = getDims(size);
				m_Model = new Model((short)xy.x, (short)xy.y);
				m_ModelCanvas.model = m_Model;
				m_ModelCanvas.update(m_ModelCanvas.getGraphics());
				
				m_ModelCanvas.nSteps = 0;
				m_ModelCanvas.removeMouseListener(m_ModelCanvas);
				m_ModelCanvas.addMouseListener(m_ModelCanvas);
				
			}

			private Point getDims(String size) {
				switch(size) {
				case "3 x 3":
					return new Point(3,3);
				case "4 x 4":
					return new Point(4,4);
				case "5 x 5":
					return new Point(5,5);
				case "6 x 6":
					return new Point(6,6);
				case "7 x 7":
					return new Point(7,7);
				case "8 x 8":
					return new Point(8,8);
				case "9 x 9":
					return new Point(9,9);
				case "10 x 10":
					return new Point(10,10);
				}
				return null;
			}
		});
		
		m_RestartButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				m_Model.reset();
				m_ModelCanvas.update(m_ModelCanvas.getGraphics());
				
				m_ModelCanvas.removeMouseListener(m_ModelCanvas);
				m_ModelCanvas.addMouseListener(m_ModelCanvas);
				m_Model.FindSolution();
				m_ModelCanvas.nSteps = 0;
				
			}
			
		});
		
		m_SolutionButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				m_Model.solve = true;
				m_ModelCanvas.update(m_ModelCanvas.getGraphics());
				
			}
			
		});
		
		m_Model = new Model((short)5,(short)5);
		m_ModelCanvas = new ModelCanvas(m_Model, this);
		
		m_MainPanel = new JPanel(new BorderLayout());
		m_MainPanel.add(m_Menu, BorderLayout.NORTH);
		m_MainPanel.add(m_ModelCanvas, BorderLayout.CENTER);
		
		setContentPane(m_MainPanel);
		setSize(100,100);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		WebLookAndFeel.install();
		new LightsOut();
	}
}
