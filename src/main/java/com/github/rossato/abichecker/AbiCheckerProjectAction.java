package com.github.rossato.abichecker;

import hudson.model.AbstractProject;
import hudson.model.Actionable;
import hudson.model.ProminentProjectAction;

public class AbiCheckerProjectAction extends Actionable implements ProminentProjectAction {
    private final AbstractProject<?, ?> project;

    public AbstractProject<?, ?> getProject() {
        return project;
    }

    public AbiCheckerBuildAction getLastCompletedBuildAction() {
        return project.getLastCompletedBuild().getAction(AbiCheckerBuildAction.class);
    }

    public AbiCheckerProjectAction(AbstractProject<?, ?> project) {
        this.project = project;
    }

    public String getDisplayName() {
        return "Latest ABI Compliance Report";
    }

    public String getSearchUrl() {
        return getDisplayName();
    }

    public String getIconFileName() {
        return "graph.gif";
    }

    public String getUrlName() {
        return "abichecker";
    }
}
