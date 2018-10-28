/*===============================================================================
Copyright (c) 2016 PTC, Inc. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other
countries.
===============================================================================*/

package com.vuforia.samples.SampleApplication.utils;

import android.content.res.AssetManager;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Build;
import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SampleApplicationV3DModel  extends MeshObject {

    private static final String LOGTAG = "V3DModel";

    private ByteBuffer _modelVertices;
    private ByteBuffer _modelTexCoords;
    private ByteBuffer _modelNormals;
    private ByteBuffer _modelMaterialIndices;
    private ByteBuffer _modelGroupAmbientColors;
    private ByteBuffer _modelGroupDiffuseColors;
    private ByteBuffer _modelGroupSpecularColors;
    private ByteBuffer _modelGroupDiffuseIndexes;
    private ByteBuffer _modelGroupDissolveFactor;
    private ByteBuffer _modelGroupVertexRanges;
    int _nbVertices = -1;
    int _nbGroups = -1;
    int _nbFaces = -1;
    int _nbMaterials = -1;
    float mVersion = -1;
    boolean mIsLoaded = false;

    private final int GEOMETRY_ARRAY = 0;
    private final int NORMALS_ARRAY = 1;
    private final int OBJ_MTL_EXTRA_ARRAY = 2;
    private final int OBJ_AMBIENT_ARRAY = 3;
    private final int OBJ_DIFFUSE_ARRAY = 4;

    private final int SHADERS_BUFFER_NUM = 5;
    int[] shaderBuffers = new int[SHADERS_BUFFER_NUM];

    private int objMtlProgramID;
    private int objMtlVertexHandle;
    private int objMtlNormalHandle;
    private int objMtlMvpMatrixHandle;
    private int objMtlMvMatrixHandle;
    private int objMtlNormalMatrixHandle;
    private int objMtlLightPosHandle;
    private int objMtlLightColorHandle;

    private int objMtlExtra;
    private int objMtlGroupAmbientColorsHandle;
    private int objMtlGroupDiffuseColorsHandle;
    private int objMtlGroupSpecularColorsHandle;
    private int objMtlGroupTransparencyHandle;

    public void loadModel(AssetManager assetManager, String filename)
            throws IOException
    {
        if(mIsLoaded)
            unloadModel();

        InputStream is = null;
        DataInputStream dis = null;
        try {
            is = assetManager.open(filename);
            dis = new DataInputStream(is);

            // Read magic number
            int magicNumber = dis.readInt();
            Log.d(LOGTAG, "MagicNumber: " + magicNumber);

            // Read version number
            mVersion = dis.readFloat();
            Log.d(LOGTAG, "NumVersion: " + mVersion);

            // Read vertices number
            _nbVertices = dis.readInt();
            Log.d(LOGTAG, "_nbVertices: " + _nbVertices);

            // Read faces number
            _nbFaces = dis.readInt();
            Log.d(LOGTAG, "_nbFaces: " + _nbFaces);

            // Read material number
            _nbMaterials = dis.readInt();
            Log.d(LOGTAG, "_nbMaterials: " + _nbMaterials);
            _nbGroups = _nbMaterials;

            // Read vertices
            int numFloatsToRead = _nbFaces * 3 * 3; // 3 vertices per face, 3 values per vertex x, y, z
            _modelVertices = ByteBuffer.allocateDirect(numFloatsToRead * (Float.SIZE / Byte.SIZE));
            _modelVertices.order(ByteOrder.nativeOrder());
            for(int i = 0; i < numFloatsToRead; ++i)
            {
                _modelVertices.putFloat(dis.readFloat());
            }
            _modelVertices.rewind();
            Log.d(LOGTAG, "First vertex: " + _modelVertices.getFloat(0) + "," + _modelVertices.getFloat(1) + "," + _modelVertices.getFloat(2));

            // Read normals
            numFloatsToRead = _nbFaces * 3 * 3; // 3 vertices per face, 3 values per vertex x, y, z
            _modelNormals = ByteBuffer.allocateDirect(numFloatsToRead * (Float.SIZE / Byte.SIZE));
            _modelNormals.order(ByteOrder.nativeOrder());
            for(int i = 0; i < numFloatsToRead; ++i)
            {
                _modelNormals.putFloat(dis.readFloat());
            }
            _modelNormals.rewind();
            Log.d(LOGTAG, "First normal: " + _modelNormals.getFloat(0) + "," + _modelNormals.getFloat(1) + "," + _modelNormals.getFloat(2));

            // Read texture coordinates
            numFloatsToRead = _nbFaces * 3 * 2; // 3 vertices per face, 2 values per vertex u, v
            _modelTexCoords = ByteBuffer.allocateDirect(numFloatsToRead * (Float.SIZE / Byte.SIZE));
            _modelTexCoords.order(ByteOrder.nativeOrder());
            for(int i = 0; i < numFloatsToRead; ++i)
            {
                _modelTexCoords.putFloat(dis.readFloat());
            }
            _modelTexCoords.rewind();
            Log.d(LOGTAG, "First tex coord: " + _modelTexCoords.getFloat(0) + "," + _modelTexCoords.getFloat(1));

            // Read material per face and shininess
            numFloatsToRead = _nbFaces * 3 * 2; // 3 vertices per face, 2 values per vertex material, shininess
            _modelMaterialIndices = ByteBuffer.allocateDirect(numFloatsToRead * (Float.SIZE / Byte.SIZE));
            _modelMaterialIndices.order(ByteOrder.nativeOrder());
            for(int i = 0; i < numFloatsToRead; ++i)
            {
                _modelMaterialIndices.putFloat(dis.readFloat());
            }
            _modelMaterialIndices.rewind();
            Log.d(LOGTAG, "First material and shininess: " + _modelMaterialIndices.getFloat(0) + "," + _modelMaterialIndices.getFloat(1));

            // Read material ambient color
            numFloatsToRead = _nbMaterials * 4; // 4 values per material r, g, b, a
            _modelGroupAmbientColors = ByteBuffer.allocateDirect(numFloatsToRead * (Float.SIZE / Byte.SIZE));
            _modelGroupAmbientColors.order(ByteOrder.nativeOrder());
            for(int i = 0; i < numFloatsToRead; ++i)
            {
                _modelGroupAmbientColors.putFloat(dis.readFloat());
            }
            _modelGroupAmbientColors.rewind();
            Log.d(LOGTAG, "First ambient color: " + _modelGroupAmbientColors.getFloat(0) + "," + _modelGroupAmbientColors.getFloat(1) + "," + _modelGroupAmbientColors.getFloat(2) + "," + _modelGroupAmbientColors.getFloat(3));

            // Read material diffuse color
            numFloatsToRead = _nbMaterials * 4; // 4 values per material r, g, b, a
            _modelGroupDiffuseColors = ByteBuffer.allocateDirect(numFloatsToRead * (Float.SIZE / Byte.SIZE));
            _modelGroupDiffuseColors.order(ByteOrder.nativeOrder());
            for(int i = 0; i < numFloatsToRead; ++i)
            {
                _modelGroupDiffuseColors.putFloat(dis.readFloat());
            }
            _modelGroupDiffuseColors.rewind();
            Log.d(LOGTAG, "First diffuse color: " + _modelGroupDiffuseColors.getFloat(0) + "," + _modelGroupDiffuseColors.getFloat(1) + "," + _modelGroupDiffuseColors.getFloat(2) + "," + _modelGroupDiffuseColors.getFloat(3));

            // Read material specular color
            numFloatsToRead = _nbMaterials * 4; // 4 values per material r, g, b, a
            _modelGroupSpecularColors = ByteBuffer.allocateDirect(numFloatsToRead * (Float.SIZE / Byte.SIZE));
            _modelGroupSpecularColors.order(ByteOrder.nativeOrder());
            for(int i = 0; i < numFloatsToRead; ++i)
            {
                _modelGroupSpecularColors.putFloat(dis.readFloat());
            }
            _modelGroupSpecularColors.rewind();
            Log.d(LOGTAG, "First specular color: " + _modelGroupSpecularColors.getFloat(0) + "," + _modelGroupSpecularColors.getFloat(1) + "," + _modelGroupSpecularColors.getFloat(2) + "," + _modelGroupSpecularColors.getFloat(3));

            // Read material diffuse texture indexes
            numFloatsToRead = _nbMaterials; // 1 index per material
            _modelGroupDiffuseIndexes = ByteBuffer.allocateDirect(numFloatsToRead * (Integer.SIZE / Byte.SIZE));
            _modelGroupDiffuseIndexes.order(ByteOrder.nativeOrder());
            for(int i = 0; i < numFloatsToRead; ++i)
            {
                _modelGroupDiffuseIndexes.putInt(dis.readInt());
            }
            _modelGroupDiffuseIndexes.rewind();
            Log.d(LOGTAG, "First material diffuse texture index: " + _modelGroupDiffuseIndexes.getInt(0));

            // Read material dissolve value (transparency)
            numFloatsToRead = _nbMaterials; // 1 value per material
            _modelGroupDissolveFactor = ByteBuffer.allocateDirect(numFloatsToRead * (Float.SIZE / Byte.SIZE));
            _modelGroupDissolveFactor.order(ByteOrder.nativeOrder());
            for(int i = 0; i < numFloatsToRead; ++i)
            {
                _modelGroupDissolveFactor.putFloat(dis.readFloat());
            }
            _modelGroupDissolveFactor.rewind();
            Log.d(LOGTAG, "First material dissolve: " + _modelGroupDissolveFactor.getFloat(0));

            // Read vertex range per group
            numFloatsToRead = _nbMaterials * 2; // 1 index per material
            _modelGroupVertexRanges = ByteBuffer.allocateDirect(numFloatsToRead * (Integer.SIZE / Byte.SIZE));
            _modelGroupVertexRanges.order(ByteOrder.nativeOrder());
            for(int i = 0; i < numFloatsToRead; ++i)
            {
                _modelGroupVertexRanges.putInt(dis.readInt());
            }
            _modelGroupVertexRanges.rewind();
            Log.d(LOGTAG, "First material diffuse texture index: " + _modelGroupVertexRanges.getInt(0) + "," + _modelGroupVertexRanges.getInt(1));

            // Read magic number again
            int magicNumberEnd = dis.readInt();
            Log.d(LOGTAG, "MagicNumber: " + magicNumber);

            if( magicNumberEnd == magicNumber )
            {
                initShaders();

                mIsLoaded = true;

                Log.d(LOGTAG, "All file was read correctly.");
            }
            else
            {
                unloadModel();
                Log.d(LOGTAG, "An error occurred during v3d file.");
            }

        } finally
        {
            if (dis != null)
                dis.close();

            if (is != null)
                is.close();
        }

    }

    public void unloadModel()
    {
        if(!mIsLoaded)
            return;

        mIsLoaded = false;
        mVersion = -1;
        _nbVertices = -1;
        _nbFaces = -1;
        _nbMaterials = -1;
        _modelVertices = null;
        _modelNormals = null;
        _modelTexCoords = null;
        _modelMaterialIndices = null;
        _modelGroupAmbientColors = null;
        _modelGroupDiffuseColors = null;
        _modelGroupSpecularColors = null;
        _modelGroupDiffuseIndexes = null;
        _modelGroupDissolveFactor = null;
        _modelGroupVertexRanges = null;

        GLES20.glDeleteBuffers(SHADERS_BUFFER_NUM, shaderBuffers, 0);
    }

    private boolean initShaders() {
        Log.d(LOGTAG, "initShaders");

        GLES20.glGenBuffers(SHADERS_BUFFER_NUM, shaderBuffers, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, shaderBuffers[GEOMETRY_ARRAY]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, _nbVertices * 3 * (Float.SIZE / Byte.SIZE), _modelVertices, GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, shaderBuffers[NORMALS_ARRAY]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, _nbVertices * 3 * (Float.SIZE / Byte.SIZE), _modelNormals, GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, shaderBuffers[OBJ_MTL_EXTRA_ARRAY]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, _nbVertices * 2 * (Float.SIZE / Byte.SIZE), _modelMaterialIndices, GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, shaderBuffers[OBJ_AMBIENT_ARRAY]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, _nbGroups * (Float.SIZE / Byte.SIZE), _modelGroupAmbientColors, GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, shaderBuffers[OBJ_DIFFUSE_ARRAY]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, _nbGroups * (Float.SIZE / Byte.SIZE), _modelGroupDiffuseColors, GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);


        // objmtl program
        objMtlProgramID = SampleUtils.createProgramFromShaderSrc(
                LightingShaders.LIGHTING_VERTEX_SHADER,
                LightingShaders.LIGHTING_FRAGMENT_SHADER);

        SampleUtils.checkGLError("v3d GLInitRendering #0");

        objMtlVertexHandle = GLES20.glGetAttribLocation(objMtlProgramID, "a_position");
        objMtlNormalHandle = GLES20.glGetAttribLocation(objMtlProgramID, "a_normal");
        objMtlExtra = GLES20.glGetAttribLocation(objMtlProgramID, "a_vertexExtra");

        Log.d(LOGTAG, ">GL> objMtlVertexHandle= " + objMtlVertexHandle);
        Log.d(LOGTAG, ">GL> objMtlExtra= " + objMtlExtra);

        objMtlMvpMatrixHandle = GLES20.glGetUniformLocation(objMtlProgramID,
                "u_mvpMatrix");
        objMtlMvMatrixHandle = GLES20.glGetUniformLocation(objMtlProgramID, "u_mvMatrix");
        objMtlNormalMatrixHandle = GLES20.glGetUniformLocation(objMtlProgramID,
                "u_normalMatrix");

        objMtlLightPosHandle = GLES20.glGetUniformLocation(objMtlProgramID, "u_lightPos");
        objMtlLightColorHandle = GLES20.glGetUniformLocation(objMtlProgramID,
                "u_lightColor");

        objMtlGroupAmbientColorsHandle = GLES20.glGetUniformLocation(objMtlProgramID,
                "u_groupAmbientColors");
        objMtlGroupDiffuseColorsHandle = GLES20.glGetUniformLocation(objMtlProgramID,
                "u_groupDiffuseColors");
        objMtlGroupSpecularColorsHandle = GLES20.glGetUniformLocation(objMtlProgramID,
                "u_groupSpecularColors");
        objMtlGroupTransparencyHandle = GLES20.glGetUniformLocation(objMtlProgramID,
                "u_groupTransparency");

        SampleUtils.checkGLError("v3d GLInitRendering #1");

        int total[] = new int[1];
        GLES20.glGetProgramiv(objMtlProgramID, GLES20.GL_ACTIVE_UNIFORMS, total, 0);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN)
        {
            Log.d(LOGTAG, "@@ nb uniforms: " + total[0]);
            for (int i = 0; i < total[0]; ++i) {
                int[] uniformType = new int[1];
                int[] uniformSize = new int[1];
                String name = GLES20.glGetActiveUniform(objMtlProgramID, i, uniformSize, 0, uniformType, 0);
                int location = GLES20.glGetUniformLocation(objMtlProgramID, name);
                Log.d(LOGTAG, "@@ uniform(" + name + "), location= " + location);
            }
        }

        Log.d(LOGTAG, "end of initShaders");

        return true;
    }

    public boolean render(float[] modelViewMatrix, float[] modelViewProjMatrix)
    {
        GLES20.glUseProgram(objMtlProgramID);

        if(mIsLoaded) {
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, shaderBuffers[GEOMETRY_ARRAY]);
            GLES20.glVertexAttribPointer(objMtlVertexHandle, 3, GLES20.GL_FLOAT, false, 0,
                    0);
            GLES20.glEnableVertexAttribArray(objMtlVertexHandle);

            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, shaderBuffers[NORMALS_ARRAY]);
            GLES20.glVertexAttribPointer(objMtlNormalHandle, 3, GLES20.GL_FLOAT, false, 0,
                    0);
            GLES20.glEnableVertexAttribArray(objMtlNormalHandle);

            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, shaderBuffers[OBJ_MTL_EXTRA_ARRAY]);
            GLES20.glVertexAttribPointer(objMtlExtra, 2, GLES20.GL_FLOAT, false, 0,
                    0);
            GLES20.glEnableVertexAttribArray(objMtlExtra);

            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);


            if(objMtlMvpMatrixHandle >= 0) {
                GLES20.glUniformMatrix4fv(objMtlMvpMatrixHandle, 1, false,
                        modelViewProjMatrix, 0);
            }

            GLES20.glUniformMatrix4fv(objMtlMvMatrixHandle, 1, false,
                    modelViewMatrix, 0);

            // compute normal matrix
            float[] inverseMatrix = new float[16];
            Matrix.invertM(inverseMatrix, 0, modelViewMatrix, 0);

            float[] normalMatrix = new float[16];
            Matrix.transposeM(normalMatrix, 0, inverseMatrix, 0);

            GLES20.glUniformMatrix4fv(objMtlNormalMatrixHandle, 1, false,
                    normalMatrix, 0);

            GLES20.glUniform4fv(objMtlGroupAmbientColorsHandle, _nbGroups,
                    _modelGroupAmbientColors.asFloatBuffer());
            GLES20.glUniform4fv(objMtlGroupDiffuseColorsHandle, _nbGroups,
                    _modelGroupDiffuseColors.asFloatBuffer());

            GLES20.glUniform4fv(objMtlGroupSpecularColorsHandle, _nbGroups,
                    _modelGroupSpecularColors.asFloatBuffer());
            GLES20.glUniform1fv(objMtlGroupTransparencyHandle, _nbGroups,
                    _modelGroupDissolveFactor.asFloatBuffer());

            GLES20.glUniform4f(objMtlLightPosHandle, 0.2f, -1.0f, 0.5f, -1.0f);
            GLES20.glUniform4f(objMtlLightColorHandle, 0.5f, 0.5f, 0.5f, 1.0f);

            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, _nbVertices);

            GLES20.glDisableVertexAttribArray(objMtlVertexHandle);
            GLES20.glDisableVertexAttribArray(objMtlNormalHandle);
            GLES20.glDisableVertexAttribArray(objMtlExtra);
        }
        else {
            Log.d(LOGTAG, "Not Rendering V3d");
        }

        SampleUtils.checkGLError("v3d renderFrame");

        return true;
    }

    @Override
    public Buffer getBuffer(BUFFER_TYPE bufferType)
    {
        Buffer result = null;
        switch (bufferType)
        {
            case BUFFER_TYPE_VERTEX:
                result = _modelVertices;
                break;
            case BUFFER_TYPE_TEXTURE_COORD:
                result = _modelTexCoords;
                break;
            case BUFFER_TYPE_NORMALS:
                result = _modelNormals;
            default:
                break;
        }
        return result;
    }

    @Override
    public int getNumObjectVertex()
    {
        return _nbVertices;
    }

    @Override
    public int getNumObjectIndex()
    {
        return 0;
    }

}
