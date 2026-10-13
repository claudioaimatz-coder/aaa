package io.github.Memoires.trbeyond.client.collision;

import com.mojang.math.Matrix3f;
import com.mojang.math.Vector3f;
import java.util.Arrays;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class OBB {
   public Vec3 center;
   public Vec3 extent;
   public Vec3 axisX;
   public Vec3 axisY;
   public Vec3 axisZ;
   public Vec3 scaledAxisX;
   public Vec3 scaledAxisY;
   public Vec3 scaledAxisZ;
   public Matrix3f rotation = new Matrix3f();
   public Vec3 vertex1;
   public Vec3 vertex2;
   public Vec3 vertex3;
   public Vec3 vertex4;
   public Vec3 vertex5;
   public Vec3 vertex6;
   public Vec3 vertex7;
   public Vec3 vertex8;
   public Vec3[] vertices;

   public OBB(Vec3 center, double width, double height, double depth, float yaw, float pitch) {
      this.center = center;
      this.extent = new Vec3(width / 2.0, height / 2.0, depth / 2.0);
      this.axisZ = Vec3.directionFromRotation(yaw, pitch).normalize();
      this.axisY = Vec3.directionFromRotation(yaw + 90.0F, pitch).reverse().normalize();
      this.axisX = this.axisZ.cross(this.axisY);
   }

   public OBB(Vec3 center, Vec3 size, float yaw, float pitch) {
      this(center, size.x, size.y, size.z, yaw, pitch);
   }

   public OBB(AABB box) {
      this.center = new Vec3((box.maxX + box.minX) / 2.0, (box.maxY + box.minY) / 2.0, (box.maxZ + box.minZ) / 2.0);
      this.extent = new Vec3(
         Math.abs(box.maxX - box.minX) / 2.0, Math.abs(box.maxY - box.minY) / 2.0, Math.abs(box.maxZ - box.minZ) / 2.0
      );
      this.axisX = new Vec3(1.0, 0.0, 0.0);
      this.axisY = new Vec3(0.0, 1.0, 0.0);
      this.axisZ = new Vec3(0.0, 0.0, 1.0);
   }

   public OBB(OBB obb) {
      this.center = obb.center;
      this.extent = obb.extent;
      this.axisX = obb.axisX;
      this.axisY = obb.axisY;
      this.axisZ = obb.axisZ;
   }

   public OBB copy() {
      return new OBB(this);
   }

   public OBB offsetAlongAxisX(double offset) {
      this.center = this.center.add(this.axisX.scale(offset));
      return this;
   }

   public OBB offsetAlongAxisY(double offset) {
      this.center = this.center.add(this.axisY.scale(offset));
      return this;
   }

   public OBB offsetAlongAxisZ(double offset) {
      this.center = this.center.add(this.axisZ.scale(offset));
      return this;
   }

   public OBB OffsetAlongAxisZ(double offset) {
      this.center = this.center.subtract(this.axisZ.scale(offset));
      return this;
   }

   public OBB offset(Vec3 offset) {
      this.center = this.center.add(offset);
      return this;
   }

   public OBB scale(double scale) {
      this.extent = this.extent.scale(scale);
      return this;
   }

   public OBB updateVertex() {
      this.rotation.set(0, 0, (float)this.axisX.x);
      this.rotation.set(0, 1, (float)this.axisX.y);
      this.rotation.set(0, 2, (float)this.axisX.z);
      this.rotation.set(1, 0, (float)this.axisY.x);
      this.rotation.set(1, 1, (float)this.axisY.y);
      this.rotation.set(1, 2, (float)this.axisY.z);
      this.rotation.set(2, 0, (float)this.axisZ.x);
      this.rotation.set(2, 1, (float)this.axisZ.y);
      this.rotation.set(2, 2, (float)this.axisZ.z);
      this.scaledAxisX = this.axisX.scale(this.extent.x);
      this.scaledAxisY = this.axisY.scale(this.extent.y);
      this.scaledAxisZ = this.axisZ.scale(this.extent.z);
      this.vertex1 = this.center.subtract(this.scaledAxisZ).subtract(this.scaledAxisX).subtract(this.scaledAxisY);
      this.vertex2 = this.center.subtract(this.scaledAxisZ).add(this.scaledAxisX).subtract(this.scaledAxisY);
      this.vertex3 = this.center.subtract(this.scaledAxisZ).add(this.scaledAxisX).add(this.scaledAxisY);
      this.vertex4 = this.center.subtract(this.scaledAxisZ).subtract(this.scaledAxisX).add(this.scaledAxisY);
      this.vertex5 = this.center.add(this.scaledAxisZ).subtract(this.scaledAxisX).subtract(this.scaledAxisY);
      this.vertex6 = this.center.add(this.scaledAxisZ).add(this.scaledAxisX).subtract(this.scaledAxisY);
      this.vertex7 = this.center.add(this.scaledAxisZ).add(this.scaledAxisX).add(this.scaledAxisY);
      this.vertex8 = this.center.add(this.scaledAxisZ).subtract(this.scaledAxisX).add(this.scaledAxisY);
      this.vertices = new Vec3[]{this.vertex1, this.vertex2, this.vertex3, this.vertex4, this.vertex5, this.vertex6, this.vertex7, this.vertex8};
      return this;
   }

   public boolean contains(Vec3 point) {
      Vector3f distance = new Vector3f(point.subtract(this.center));
      distance.transform(this.rotation);
      return (double)Math.abs(distance.x()) < this.extent.x
         && (double)Math.abs(distance.y()) < this.extent.y
         && (double)Math.abs(distance.z()) < this.extent.z;
   }

   public boolean intersects(AABB boundingBox) {
      OBB otherOBB = new OBB(boundingBox).updateVertex();
      return Intersects(this, otherOBB);
   }

   public boolean intersects(OBB otherOBB) {
      return Intersects(this, otherOBB);
   }

   public static boolean Intersects(OBB a, OBB b) {
      if (Separated(a.vertices, b.vertices, a.scaledAxisX)) {
         return false;
      } else if (Separated(a.vertices, b.vertices, a.scaledAxisY)) {
         return false;
      } else if (Separated(a.vertices, b.vertices, a.scaledAxisZ)) {
         return false;
      } else if (Separated(a.vertices, b.vertices, b.scaledAxisX)) {
         return false;
      } else if (Separated(a.vertices, b.vertices, b.scaledAxisY)) {
         return false;
      } else if (Separated(a.vertices, b.vertices, b.scaledAxisZ)) {
         return false;
      } else if (Separated(a.vertices, b.vertices, a.scaledAxisX.cross(b.scaledAxisX))) {
         return false;
      } else if (Separated(a.vertices, b.vertices, a.scaledAxisX.cross(b.scaledAxisY))) {
         return false;
      } else if (Separated(a.vertices, b.vertices, a.scaledAxisX.cross(b.scaledAxisZ))) {
         return false;
      } else if (Separated(a.vertices, b.vertices, a.scaledAxisY.cross(b.scaledAxisX))) {
         return false;
      } else if (Separated(a.vertices, b.vertices, a.scaledAxisY.cross(b.scaledAxisY))) {
         return false;
      } else if (Separated(a.vertices, b.vertices, a.scaledAxisY.cross(b.scaledAxisZ))) {
         return false;
      } else if (Separated(a.vertices, b.vertices, a.scaledAxisZ.cross(b.scaledAxisX))) {
         return false;
      } else {
         return Separated(a.vertices, b.vertices, a.scaledAxisZ.cross(b.scaledAxisY))
            ? false
            : !Separated(a.vertices, b.vertices, a.scaledAxisZ.cross(b.scaledAxisZ));
      }
   }

   private static boolean Separated(Vec3[] vertsA, Vec3[] vertsB, Vec3 axis) {
      if (axis.equals(Vec3.ZERO)) {
         return false;
      } else {
         double aMin = Double.POSITIVE_INFINITY;
         double aMax = Double.NEGATIVE_INFINITY;
         double bMin = Double.POSITIVE_INFINITY;
         double bMax = Double.NEGATIVE_INFINITY;

         for (int i = 0; i < 8; i++) {
            double aDist = vertsA[i].dot(axis);
            aMin = aDist < aMin ? aDist : aMin;
            aMax = aDist > aMax ? aDist : aMax;
            double bDist = vertsB[i].dot(axis);
            bMin = bDist < bMin ? bDist : bMin;
            bMax = bDist > bMax ? bDist : bMax;
         }

         double longSpan = Math.max(aMax, bMax) - Math.min(aMin, bMin);
         double sumSpan = aMax - aMin + bMax - bMin;
         return longSpan >= sumSpan;
      }
   }

   @Override
   public String toString() {
      return "OBB{\ncenter="
         + this.center
         + ", \nextent="
         + this.extent
         + ", \naxisX="
         + this.axisX
         + ", \naxisY="
         + this.axisY
         + ", \naxisZ="
         + this.axisZ
         + ", \nscaledAxisX="
         + this.scaledAxisX
         + ", \nscaledAxisY="
         + this.scaledAxisY
         + ", \nscaledAxisZ="
         + this.scaledAxisZ
         + ", \nrotation="
         + this.rotation
         + ", \nvertex1="
         + this.vertex1
         + ", \nvertex2="
         + this.vertex2
         + ", \nvertex3="
         + this.vertex3
         + ", \nvertex4="
         + this.vertex4
         + ", \nvertex5="
         + this.vertex5
         + ", \nvertex6="
         + this.vertex6
         + ", \nvertex7="
         + this.vertex7
         + ", \nvertex8="
         + this.vertex8
         + ", \nvertices="
         + Arrays.toString((Object[])this.vertices)
         + "\n}";
   }
}
