package me.restonic4.engine.util.debug;

import static org.lwjgl.opengl.GL43.*;

import org.lwjgl.opengl.GLDebugMessageCallback;

public class OpenGLDebugOutput {
    private GLDebugMessageCallback debugCallback;

    public void setupDebugMessageCallback() {
        debugCallback = GLDebugMessageCallback.create((source, type, id, severity, length, message, userParam) -> {
            Logger.log("--------------------------------------------");
            Logger.log("OpenGL DEBUG MESSAGE:");
            Logger.log("Source: " + getSourceString(source));
            Logger.log("Type: " + getTypeString(type));
            Logger.log("ID: " + id);
            Logger.log("Severity: " + getSeverityString(severity));
            Logger.log("Message: " + GLDebugMessageCallback.getMessage(length, message));
            Logger.log("--------------------------------------------");
        });

        glDebugMessageCallback(debugCallback, 0);
    }

    private String getSourceString(int source) {
        return switch (source) {
            case GL_DEBUG_SOURCE_API -> "API";
            case GL_DEBUG_SOURCE_WINDOW_SYSTEM -> "Window System";
            case GL_DEBUG_SOURCE_SHADER_COMPILER -> "Shader Compiler";
            case GL_DEBUG_SOURCE_THIRD_PARTY -> "Third Party";
            case GL_DEBUG_SOURCE_APPLICATION -> "Application";
            case GL_DEBUG_SOURCE_OTHER -> "Other";
            default -> "Unknown";
        };
    }

    private String getTypeString(int type) {
        return switch (type) {
            case GL_DEBUG_TYPE_ERROR -> "Error";
            case GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR -> "Deprecated Behavior";
            case GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR -> "Undefined Behavior";
            case GL_DEBUG_TYPE_PORTABILITY -> "Portability";
            case GL_DEBUG_TYPE_PERFORMANCE -> "Performance";
            case GL_DEBUG_TYPE_MARKER -> "Marker";
            case GL_DEBUG_TYPE_PUSH_GROUP -> "Push Group";
            case GL_DEBUG_TYPE_POP_GROUP -> "Pop Group";
            case GL_DEBUG_TYPE_OTHER -> "Other";
            default -> "Unknown";
        };
    }

    private String getSeverityString(int severity) {
        return switch (severity) {
            case GL_DEBUG_SEVERITY_HIGH -> "High";
            case GL_DEBUG_SEVERITY_MEDIUM -> "Medium";
            case GL_DEBUG_SEVERITY_LOW -> "Low";
            case GL_DEBUG_SEVERITY_NOTIFICATION -> "Notification";
            default -> "Unknown";
        };
    }
}
