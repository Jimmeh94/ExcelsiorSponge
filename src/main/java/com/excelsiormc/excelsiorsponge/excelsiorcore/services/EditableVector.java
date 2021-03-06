package com.excelsiormc.excelsiorsponge.excelsiorcore.services;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;

public class EditableVector {

    private double x, y, z;

    public EditableVector(Vector3d vector3d){
        this(vector3d.getX(), vector3d.getY(), vector3d.getZ());
    }

    public EditableVector(Vector3i v){
        this(v.getX(), v.getY(), v.getZ());
    }

    public EditableVector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void subtract(double x, double y, double z){
        this.x -= x;
        this.y -= y;
        this.z -= z;
    }

    public void add(double x, double y, double z){
        this.x += x;
        this.y += y;
        this.z += z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public EditableVector clone(){
        return new EditableVector(x, y, z);
    }

    public Vector3d toVector3d(){
        return new Vector3d(x, y, z);
    }
}
