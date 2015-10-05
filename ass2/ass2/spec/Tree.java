package ass2.spec;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import com.jogamp.opengl.util.gl2.GLUT;

/**
 * COMMENT: Comment Tree 
 *
 * @author malcolmr
 */
public class Tree {

    private double[] myPos;
    
    public Tree(double x, double y, double z) {
        myPos = new double[3];
        myPos[0] = x;
        myPos[1] = y;
        myPos[2] = z;
    }
    
    public double[] getPosition() {
        return myPos;
    }
    
    public void draw(GL2 gl) { 
    	gl.glPushMatrix();
    	
    	GLU glu = new GLU();
	    GLUT glut = new GLUT();
		
	    gl.glTranslated (myPos[0], myPos[1],  myPos[2]);    	    
	    gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);

        gl.glRotated(270, 1, 0, 0);  
        glut.glutSolidCylinder(0.2,1.2,24,24); 
        
	    gl.glTranslated(0,0,1.2);  
	    glut.glutSolidSphere(0.55, 24, 24);


	    
	    gl.glPopMatrix();
}

}
