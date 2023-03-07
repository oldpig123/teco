package org.teco;

import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import com.facebook.nailgun.NGContext;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

public class AdHocRunnerGeneratorDepth1 {

    public void generate(String srcPath, String className, String methodName, String outPath,
        String logPath) throws Exception {
        AdHocRunnerGeneratorDepth1Modifier.Context context =
            new AdHocRunnerGeneratorDepth1Modifier.Context();
        context.className = className;
        context.methodName = methodName;
        context.logPath = logPath;

        AdHocRunnerGeneratorDepth1Modifier visitor = new AdHocRunnerGeneratorDepth1Modifier();
        CompilationUnit cu = StaticJavaParser.parse(Paths.get(srcPath));
        cu = (CompilationUnit) cu.accept(visitor, context);
        FileWriter writer = new FileWriter(outPath);
        writer.write(cu.toString());
        writer.close();
    }

    // nailgun entry point

    static AdHocRunnerGeneratorDepth1 instance = new AdHocRunnerGeneratorDepth1();

    public static void nailMain(NGContext context) throws Exception {
        String[] args = context.getArgs();
        if (args.length == 0) {
            throw new RuntimeException("Usage(nailgun): AcHocRunnerGeneratorDepth1 action args");
        }

        String action = args[0];
        List<String> otherArgs = Arrays.asList(args).subList(1, args.length);

        try {
            switch (action) {
                case "generate":
                    instance.generate(
                        otherArgs.get(0), otherArgs.get(1), otherArgs.get(2), otherArgs.get(3),
                        otherArgs.get(4));
                    return;
            }
        } catch (Exception e) {
            System.err.println("Exception during " + action + ": " + e.getClass());
            e.printStackTrace();
            throw e;
        }
        throw new RuntimeException("unknown command: " + context.getCommand());
    }
}
