package me.restonic4.engine

public class SceneManager {
private SceneManager instance;

private Scene currentScene;

public Scene getInstance() {
if (this.instance == null) {
this.instance = new SceneManager();
}
return this.instance;
}

public Scene getScene() {
return this.currentScene;
}
}