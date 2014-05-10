package de.abscanvas.internal;

import de.abscanvas.entity.LevelEntity;

public class EntityRegister {
	private final Class<? extends LevelEntity> c;
	private final short cid;
	
	public EntityRegister(Class<? extends LevelEntity> c, short cid) {
		this.c = c;
		this.cid = cid;
	}
	
	public short getClassID() {
		return cid;
	}
	
	public short getCID() {
		return cid;
	}

	public Class<? extends LevelEntity> getC() {
		return c;
	}
	
	public LevelEntity create() {
		return (LevelEntity) ClassHelper.getObject(c);
	}
}
