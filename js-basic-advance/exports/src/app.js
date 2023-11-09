import {ProjectList} from './app/ProjectList.js'
import {ProjectType} from './app/ProjectType.js';

class App {
    static init() {
        const activeProjects = new ProjectList(ProjectType.ACTIVE);
        const finishedProjects = new ProjectList(ProjectType.FINISHED);
        activeProjects.setSwitchHanlderCallback(finishedProjects.addProject.bind(finishedProjects));
        finishedProjects.setSwitchHanlderCallback(activeProjects.addProject.bind(activeProjects));
    }
}

App.init();