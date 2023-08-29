package net.frozenblock.configurableeverything.config.gui.api;

import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.minecraft.world.phys.Vec3;
import java.util.List;

public class MutableVec3 {
	public static final Codec<MutableVec3> CODEC = Codec.DOUBLE.listOf().comapFlatMap(list -> Util.fixedSize(list, 3).map(vectors -> new MutableVec3(vectors.get(0), vectors.get(1), vectors.get(2))), vec -> List.of(vec.getX(), vec.getY(), vec.getZ()));

	public double x;
	public double y;
	public double z;

	public MutableVec3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vec3 asVec3() {
		return new Vec3(this.x, this.y, this.z);
	}

	public double getX() {
		return this.x;
	}

	public void setX(double d) {
		this.x = d;
	}

	public double getY() {
		return this.y;
	}

	public void setY(double d) {
		this.y = d;
	}

	public double getZ() {
		return this.z;
	}

	public void setZ(double d) {
		this.z = d;
	}
}
