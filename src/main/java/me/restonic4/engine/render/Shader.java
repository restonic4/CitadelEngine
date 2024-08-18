package me.restonic4.engine.render;

import me.restonic4.engine.platform.PlatformManager;
import me.restonic4.engine.util.FileManager;
import me.restonic4.engine.util.debug.Logger;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;

public class Shader {
    private String filepath;

    private int shaderProgramID;
    private String vertexSource;
    private String fragmentSource;

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
            throw new RuntimeException("Error: Could not open file for shader: '" + filepath + "'");
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

            throw new RuntimeException();
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

            throw new RuntimeException();
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

            throw new RuntimeException();
        }
    }

    public void use() {
        // Bind shader program
        glUseProgram(this.shaderProgramID);
    }

    public void detach() {
        glUseProgram(0);
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
}
