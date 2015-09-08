package net.eitr.gin.ship;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;

import net.eitr.gin.Units.*;
import net.eitr.gin.network.CircleData;
import net.eitr.gin.network.RectData;
import net.eitr.gin.network.ShapeData;
import net.eitr.gin.network.ShipData;

public class ShipPart {

	Body ship;
	DrawShapeType drawType;
	public ShipPartType type;
	Vector2 pos;
	int id;
	float width,height,angle,radius;
	public float health;


	public ShipPart (int i, Body b, ShipPartType t, Vector2 p, float w, float h, float a) {
		init(i,b,t,p);
		drawType = DrawShapeType.RECT;
		width = w;
		height = h;
		angle = a;
	}

	public ShipPart (int i, Body b, ShipPartType t, Vector2 p, float r) {
		init(i,b,t,p);
		drawType = DrawShapeType.CIRCLE;
		radius = r;
	}

	private void init (int i, Body b, ShipPartType t, Vector2 p) {
		id = i;
		ship = b;
		type = t;
		pos = p;
		health = 100;
	}
	
	protected void update (boolean isShooting) {}
	
	//TODO polygon intersection
	public boolean intersects (ShipPart part) {
		if (drawType == DrawShapeType.CIRCLE && part.drawType == DrawShapeType.CIRCLE) {
			return Intersector.overlaps(new Circle(pos,radius), new Circle(part.pos,part.radius));
		}
		else if (drawType == DrawShapeType.RECT && part.drawType == DrawShapeType.CIRCLE) {
			return Intersector.overlaps(new Circle(part.pos,part.radius),new Rectangle(pos.x-width/2,pos.y-height/2,width,height));
		}
		else if (drawType == DrawShapeType.CIRCLE && part.drawType == DrawShapeType.RECT) {
			return Intersector.overlaps(new Circle(pos,radius),new Rectangle(part.pos.x-part.width/2,part.pos.y-part.height/2,part.width,part.height));
		}
		else if (drawType == DrawShapeType.RECT && part.drawType == DrawShapeType.RECT) {
			return Intersector.overlaps(new Rectangle(pos.x-width/2,pos.y-height/2,width,height),new Rectangle(part.pos.x-part.width/2,part.pos.y-part.height/2,part.width,part.height));
		}
		return false;
	}
	
	public void damage (float damage) {
		health = MathUtils.clamp(health-damage, 0, 100);
	}
	
	public int getId () {
		return id;
	}

	public void getGraphics (ShipData shipData) {
		ShapeData shape = null;
		switch(drawType) {
		case CIRCLE: shape = new CircleData(pos.x, pos.y, radius); break;
		case RECT: shape = new RectData(pos.x-width/2f, pos.y-height/2f, width, height); break;
		default: return;
		}
		shape.setColor(0, health/100f, health/100f, 1);
		shipData.parts.add(shape);
	}
}
