package me.restonic4.citadel.render;

import me.restonic4.ClientSide;
import me.restonic4.citadel.exceptions.RenderException;
import me.restonic4.citadel.platform.PlatformManager;
import me.restonic4.citadel.files.FileManager;
import me.restonic4.citadel.registries.RegistryObject;
import me.restonic4.citadel.util.CitadelConstants;
import me.restonic4.citadel.util.debug.diagnosis.Logger;

import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;

@ClientSide
public class Shader extends RegistryObject {
    private String filepath;
    private String[] desiredUniforms;

    private int shaderProgramID, vertexID, fragmentID;
    private String vertexSource;
    private String fragmentSource;
    private UniformsMap uniformsMap;
    private boolean isUsed = false;

    public Shader() {}

    public Shader(String[] uniforms) {
        this.desiredUniforms = uniforms;
    }

    @Override
    public void onPopulate() {
        super.onPopulate();

        create();
    }

    private void create() {
        this.filepath = FileManager.toResources(getAssetPath());

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

    public void generateUniforms() {
        Logger.log("Generating uniforms for " + shaderProgramID);

        for (int i = 0; i < desiredUniforms.length; i++) {
            uniformsMap.createUniform(desiredUniforms[i]);
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

        // Starting the uniform map
        uniformsMap = new UniformsMap(shaderProgramID);
        generateUniforms();
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

    public void cleanup() {
        glDeleteProgram(this.shaderProgramID);
        glDeleteShader(vertexID);
        glDeleteShader(fragmentID);
    }

    public UniformsMap getUniformsMap() {
        return this.uniformsMap;
    }
}
