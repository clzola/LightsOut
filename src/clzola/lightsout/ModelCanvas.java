package clzola.lightsout;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;

public class ModelCanvas extends Canvas implements MouseListener {
	private static final long serialVersionUID = -4535451587982761276L;
	public Model model;
	private LightsOut frame;
	
	public int nSteps = 0;
	
	public ModelCanvas(Model m, LightsOut f) {
		setBackground(Color.BLACK);
		setSize(1024,1024);
		model = m;
		frame = f;
		
		this.addMouseListener(this);
	}
	
	@Override
	public void paint(Graphics g) {
		int minX = 330;
		
		int w = model.M*Model.SIZE + (model.M-1)*Model.GAP + 2*8;
		int h = model.N*Model.SIZE + (model.N-1)*Model.GAP + 2*8;
		
		int startx = 8;
		int starty = 8;
		if(w < minX) {
			startx = (330-(w-16))/2;
			setPreferredSize(new Dimension(330,h));
			frame.pack();
		} else {
			setPreferredSize(new Dimension(w, h));
			frame.pack();
		}
		
		
		model.draw(g, startx, starty);
	}

	@Override
	public void update(Graphics g) {
		super.update(g);
		
		boolean solve = true;
		for(int i=0; i<model.M; i++)
			for(int j=0; j<model.N; j++) 
				if(model.MATRIX[i][j] == 1)
					solve = false;
		
		if( solve == true ) {
			this.removeMouseListener(this);
			 
			JOptionPane.showMessageDialog(this, "Congrats!\nYou have managed to solve puzzle in " + nSteps + " steps.\nBest solution is "+model.best+" steps.");
		}
			
		
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		for(int i=0; i<model.M; i++) {
			for(int j=0; j<model.N; j++)
				if(model.MRect[i][j].contains(event.getPoint())) {
					model.MATRIX[i][j] = invert(model.MATRIX[i][j]);
					
					if( i > 0 )   model.MATRIX[i-1][j] = invert(model.MATRIX[i-1][j]);
				    if( i < model.M-1 ) model.MATRIX[i+1][j] = invert(model.MATRIX[i+1][j]);
				    if( j > 0 )   model.MATRIX[i][j-1] = invert(model.MATRIX[i][j-1]);
				    if( j < model.N-1 ) model.MATRIX[i][j+1] = invert(model.MATRIX[i][j+1]);
				    
				    if( model.solve == true ) {
				    	model.solutionVector[i*model.N+j] = 0;
				    }
				    
				    nSteps++;
				    update(getGraphics());
				    return;
				}
		}
		
	}
	
	private short invert(short x) {
		return (short) (x == 0 ? 1 : 0);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	

}
