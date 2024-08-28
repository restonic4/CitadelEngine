package me.restonic4.citadel.render;

import me.restonic4.citadel.exceptions.RenderException;
import me.restonic4.citadel.platform.PlatformManager;
import me.restonic4.citadel.files.FileManager;
import me.restonic4.citadel.util.debug.diagnosis.Logger;
import me.restonic4.shared.SharedMathConstants;

import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;

public class Shader {
    private String filepath;

    private int shaderProgramID;
    private String vertexSource;
    private String fragmentSource;
    private boolean isUsed = false;

    public Shader(String filepath) {
        filepath = FileManager.toResources(filepath);

        this.filepath = filepath;

        try {
            // Reading the shader file and splitting it into a vertex and fragment shader
            String source = FileManager.readFile(filepath);
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");

            // Find the first pattern after #type 'pattern'
            int index = source.indexOf("#type") + 6;
            int endOfTheLine = source.indexOf(PlatformManager.getEndOfLine(), index);
            String firstPattern = source.substring(index, endOfTheLine).trim();

            // Find the second pattern after #type 'pattern'
            index = source.indexOf("#type", endOfTheLine) + 6;
            endOfTheLine = source.indexOf(PlatformManager.getEndOfLine(), index);
            String secondPattern = source.substring(index, endOfTheLine).trim();

            // Sets the shaders into the correct variables
            setShaderInPattern(firstPattern, splitString[1]);
            setShaderInPattern(secondPattern, splitString[2]);
        } catch(IOException e) {
            e.printStackTrace();
            throw new RenderException("Error: Could not open file for shader: '" + filepath + "'");
        }
    }

    // This sets the shader to the correct variable
    private void setShaderInPattern(String pattern, String shader) throws IOException {
        if (pattern.equals("vertex")) {
            this.vertexSource = shader;
        } else if (pattern.equals("fragment")) {
            this.fragmentSource = shader;
        } else {
            throw new IOException("Unexpected token '" + pattern + "'");
        }
    }

    public void compile() {
        int vertexID, fragmentID;

        // First load and compile the vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);

        // Pass the shader source to the GPU
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);

        // Check for errors in compilation
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);

            Logger.log("ERROR: '" + this.filepath + "'\n\tVertex shader compilation failed.");
            Logger.log(glGetShaderInfoLog(vertexID, len));

            throw new RenderException("Error compiling vertex shader");
        }

        // First load and compile the vertex shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);

        // Pass the shader source to the GPU
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);

        // Check for errors in compilation
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);

            Logger.log("ERROR: '" + this.filepath + "'\n\tFragment shader compilation failed.");
            Logger.log(glGetShaderInfoLog(fragmentID, len));

            throw new RenderException("Error compiling fragment shader");
        }

        // Link shaders and check for errors
        this.shaderProgramID = glCreateProgram();
        glAttachShader(this.shaderProgramID, vertexID);
        glAttachShader(this.shaderProgramID, fragmentID);
        glLinkProgram(this.shaderProgramID);

        // Check for linking errors
        success = glGetProgrami(this.shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(this.shaderProgramID, GL_INFO_LOG_LENGTH);

            Logger.log("ERROR: '" + this.filepath + "'\n\tLinking of shaders failed.");
            Logger.log(glGetProgramInfoLog(this.shaderProgramID, len));

            throw new RenderException("Error linking vertex and fragment shader");
        }
    }

    public void use() {
        if (isUsed) {
            return;
        }

        // Bind shader program
        glUseProgram(this.shaderProgramID);

        isUsed = true;
    }

    public void detach() {
        glUseProgram(0);
        isUsed = false;
    }

    public void uploadMat4f(String varName, Matrix4f mat4) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);

        use(); // Use the shader in case is not being used

        // Convert the matrix into a "simple float array"
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(SharedMathConstants.MATRIX4F_CAPACITY);
        mat4.get(matBuffer);

        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    public void uploadMat3f(String varName, Matrix3f mat3) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);

        use(); // Use the shader in case is not being used

        // Convert the matrix into a "simple float array"
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(SharedMathConstants.MATRIX3F_CAPACITY);
        mat3.get(matBuffer);

        glUniformMatrix3fv(varLocation, false, matBuffer);
    }

    public void uploadVec4f(String varName, Vector4f vec) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);

        use(); // Use the shader in case is not being used

        glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
    }

    public void uploadVec3f(String varName, Vector3f vec) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);

        use(); // Use the shader in case is not being used

        glUniform3f(varLocation, vec.x, vec.y, vec.z);
    }

    public void uploadVec2f(String varName, Vector2f vec) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);

        use(); // Use the shader in case is not being used

        glUniform2f(varLocation, vec.x, vec.y);
    }

    public void uploadFloat(String varName, float val) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);

        use(); // Use the shader in case is not being used

        glUniform1f(varLocation, val);
    }

    public void uploadInt(String varName, int val) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);

        use(); // Use the shader in case is not being used

        glUniform1i(varLocation, val);
    }
}
