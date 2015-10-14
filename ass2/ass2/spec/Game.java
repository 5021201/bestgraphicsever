package ass2.spec;

import java.io.File;
import java.io.FileNotFoundException;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;


/**
 * COMMENT: Comment Game 
 *
 * @author malcolmr
 */
public class Game extends JFrame implements GLEventListener, KeyListener {

    private Terrain myTerrain;

    public Game(Terrain terrain) {
    	super("Assignment 2");
        myTerrain = terrain;
        camera = new Camera(myTerrain);
    }
    
    /** 
     * Run the game.
     *
     */
    public void run() {
    	  GLProfile glp = GLProfile.getDefault();
          GLCapabilities caps = new GLCapabilities(glp);
          GLJPanel panel = new GLJPanel();
          panel.addGLEventListener(this);
 
          // Add an animator to call 'display' at 60fps        
          FPSAnimator animator = new FPSAnimator(60);
          animator.add(panel);
          animator.start();
          panel.addKeyListener(this);
          // the panel needs to be focusable to get key events
          panel.setFocusable(true);   
          getContentPane().add(panel);
          setSize(800, 600);        
          setVisible(true);
          setDefaultCloseOperation(EXIT_ON_CLOSE);       
    }
    
    /**
     * Load a level file and display it.
     * 
     * @param args - The first argument is a level file in JSON format
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
    	Terrain terrain = LevelIO.load(new File(args[0]));
        Game game = new Game(terrain);
        game.run();
    }

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		// do stuff with camera

		gl.glRotated(-camera.getCurrentRotation(), 0, 1, 0);
		double currentX = camera.getCurrentX();
		double currentZ = camera.getCurrentZ();
		double altitude = myTerrain.altitude(currentX, currentZ);
		gl.glTranslated(-currentX, -(2 + altitude), -currentZ);

		// draw the terrain
		drawTerrain(gl);
	}
	
	
	
	private void drawTerrain(GL2 gl) {
		int width = (int)myTerrain.size().getWidth();
		int height = (int)myTerrain.size().getHeight();
		gl.glColor4d(1, 1, 1, 1);

		double altitude;
		gl.glBegin(GL2.GL_TRIANGLES); {
		    for (int z = height - 1; z > 0; z--) {
				for (int x = width - 1; x > 0; x--) {
					// draw first triangle
					altitude = myTerrain.getGridAltitude(x, z);
					gl.glVertex3d(x, altitude, z);
					altitude = myTerrain.getGridAltitude(x-1,z);
					gl.glVertex3d(x-1, altitude, z);
					altitude = myTerrain.getGridAltitude(x-1,z-1);
					gl.glVertex3d(x-1, altitude, z-1);
					
					// draw second triangle
					altitude = myTerrain.getGridAltitude(x,z);
					gl.glVertex3d(x, altitude, z);
					altitude = myTerrain.getGridAltitude(x-1,z-1);
					gl.glVertex3d(x-1, altitude, z-1);
					altitude = myTerrain.getGridAltitude(x,z-1);
					gl.glVertex3d(x, altitude, z-1);
					

				}
			}
		}
		gl.glEnd();
    	drawTrees(gl);
	}
	
	public void drawTrees (GL2 gl) { 
		for(Tree t : myTerrain.trees()) {
			gl.glPushMatrix();
	    	double[] position = t.getPosition();
	    	
	    	GLU glu = new GLU();
		    GLUT glut = new GLUT();
			
		    gl.glTranslated (position[0], position[1],  position[2]);    	    
		    gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
	
	        gl.glRotated(270, 1, 0, 0);  
	        glut.glutSolidCylinder(0.2,1.2,24,24); 
	        
		    gl.glTranslated(0,0,1.2);  
		    glut.glutSolidSphere(0.55, 24, 24);
	
	
		    
		    gl.glPopMatrix();
		}
    }

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();	
    	//gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);

	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU glu = new GLU();
		glu.gluPerspective(80, width/height, 1, 50);
	}
	
        @Override
	public void keyPressed(KeyEvent e) {
    	        double xDistance;
    	        double zDistance;

    	        switch (e.getKeyCode()) {  
    	                case KeyEvent.VK_UP:
                               xDistance = 0.5*Math.sin(Math.toRadians(camera.getCurrentRotation()));
                               zDistance = 0.5*Math.cos(Math.toRadians(camera.getCurrentRotation()));
                               camera.changeCurrentX(-xDistance);
                               camera.changeCurrentZ(-zDistance);
		               break;
	                case KeyEvent.VK_DOWN:
                               xDistance = 0.5*Math.sin(Math.toRadians(camera.getCurrentRotation()));
                               zDistance = 0.5*Math.cos(Math.toRadians(camera.getCurrentRotation()));
		               camera.changeCurrentX(xDistance);
                               camera.changeCurrentZ(zDistance);
		               break;
	                case KeyEvent.VK_LEFT:
                               camera.changeCurrentRotation(5);
                               break;
	                case KeyEvent.VK_RIGHT:
	                       camera.changeCurrentRotation(-5);
	                       break;
	                default:
		               break;
    	}
    }



	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
