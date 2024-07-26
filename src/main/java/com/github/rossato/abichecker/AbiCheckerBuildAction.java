package com.github.rossato.abichecker;

import hudson.FilePath;
import hudson.model.AbstractBuild;
import hudson.model.Action;
import java.io.IOException;
import javax.servlet.ServletException;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

public class AbiCheckerBuildAction implements Action {
    AbstractBuild<?, ?> build;
    AbiCheckerReport summary;
    FilePath report;

    public AbstractBuild<?, ?> getBuild() {
        return build;
    }

    public AbiCheckerReport getSummary() {
        return summary;
    }

    public AbiCheckerBuildAction(AbstractBuild<?, ?> build, AbiCheckerReport summary, FilePath report) {
        this.build = build;
        this.summary = summary;
        this.report = report;
    }

    public void doFullReport(StaplerRequest req, StaplerResponse rsp)
            throws IOException, InterruptedException, ServletException {
        rsp.serveFile(req, report.read(), report.lastModified(), report.length(), "compat_report.html");
    }

    public String getDisplayName() {
        return "ABI Compliance Report";
    }

    public String getIconFileName() {
        return "graph.gif";
    }

    public String getUrlName() {
        return "abichecker";
    }
}
