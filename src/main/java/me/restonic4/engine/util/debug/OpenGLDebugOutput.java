package me.restonic4.engine.util.debug;

import static org.lwjgl.opengl.GL43.*;

import org.lwjgl.opengl.GLDebugMessageCallback;

public class OpenGLDebugOutput {
    private GLDebugMessageCallback debugCallback;

    public void setupDebugMessageCallback() {
        debugCallback = GLDebugMessageCallback.create((source, type, id, severity, length, message, userParam) -> {
            System.err.println("OpenGL DEBUG MESSAGE:");
            System.err.println("Source: " + getSourceString(source));
            System.err.println("Type: " + getTypeString(type));
            System.err.println("ID: " + id);
            System.err.println("Severity: " + getSeverityString(severity));
            System.err.println("Message: " + GLDebugMessageCallback.getMessage(length, message));
            System.err.println();
        });

        glDebugMessageCallback(debugCallback, 0);
    }

    private String getSourceString(int source) {
        switch (source) {
            case GL_DEBUG_SOURCE_API: return "API";
            case GL_DEBUG_SOURCE_WINDOW_SYSTEM: return "Window System";
            case GL_DEBUG_SOURCE_SHADER_COMPILER: return "Shader Compiler";
            case GL_DEBUG_SOURCE_THIRD_PARTY: return "Third Party";
            case GL_DEBUG_SOURCE_APPLICATION: return "Application";
            case GL_DEBUG_SOURCE_OTHER: return "Other";
            default: return "Unknown";
        }
    }

    private String getTypeString(int type) {
        switch (type) {
            case GL_DEBUG_TYPE_ERROR: return "Error";
            case GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR: return "Deprecated Behavior";
            case GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR: return "Undefined Behavior";
            case GL_DEBUG_TYPE_PORTABILITY: return "Portability";
            case GL_DEBUG_TYPE_PERFORMANCE: return "Performance";
            case GL_DEBUG_TYPE_MARKER: return "Marker";
            case GL_DEBUG_TYPE_PUSH_GROUP: return "Push Group";
            case GL_DEBUG_TYPE_POP_GROUP: return "Pop Group";
            case GL_DEBUG_TYPE_OTHER: return "Other";
            default: return "Unknown";
        }
    }

    private String getSeverityString(int severity) {
        switch (severity) {
            case GL_DEBUG_SEVERITY_HIGH: return "High";
            case GL_DEBUG_SEVERITY_MEDIUM: return "Medium";
            case GL_DEBUG_SEVERITY_LOW: return "Low";
            case GL_DEBUG_SEVERITY_NOTIFICATION: return "Notification";
            default: return "Unknown";
        }
    }
}
