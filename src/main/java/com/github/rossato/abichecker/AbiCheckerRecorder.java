package com.github.rossato.abichecker;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import org.kohsuke.stapler.DataBoundConstructor;

public class AbiCheckerRecorder extends Recorder {

    private final String reportLocation;

    @DataBoundConstructor
    public AbiCheckerRecorder(String reportLocation) {
        this.reportLocation = reportLocation;
    }

    public String getReportLocation() {
        return reportLocation;
    }

    @Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener)
            throws IOException, InterruptedException {

        // Step 1 : Get the report from the workspace

        FilePath ws = build.getWorkspace();
        if (ws == null) {
            /* Jenkins internal error? */
            return false;
        }
        FilePath reports[] = ws.list(this.reportLocation);

        if (reports.length == 0) {
            /* Report not found! */
            return false;
        }
        FilePath report = reports[0];

        // Step 1.5 : Save the report for linking

        FilePath archiveTarget = new FilePath(new File(build.getRootDir(), "compat_report.html"));
        report.copyTo(archiveTarget);

        // Step 2 : Parse the report

        AbiCheckerReport summary = new AbiCheckerReport(report);

        // Step 2.5 : Evaluate the report

        if (!summary.isCompatible()) {
            build.setResult(Result.UNSTABLE);
        }

        // Step 3 : Generate the jenkins summary

        build.getActions().add(new AbiCheckerBuildAction(build, summary, archiveTarget));

        return true;
    }

    @Override
    public Collection<? extends Action> getProjectActions(AbstractProject<?, ?> project) {
        return Collections.singletonList(new AbiCheckerProjectAction(project));
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            // Indicates that this builder can be used with all kinds of project types
            return true;
        }

        public String getDisplayName() {
            return "Publish ABI compliance report";
        }
    }
}
