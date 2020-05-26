package com.hzx.agent;

import javassist.*;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class AgentMain {

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("This is my first agent.");
        inst.addTransformer(new DefineTransformer(), true);
    }

    public static void premain(String agentArgs) {
        System.out.println("I think this step cannot be execute!");
    }

    static class DefineTransformer implements ClassFileTransformer {

        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                                ProtectionDomain protectionDomain, byte[] classfileBuffer) {
//            System.out.println("premain load Class:" + className);
            className = className.replace("/", ".");

            if ("com.hzx.data.DataApplication".equals(className)) {
                try {
                    CtClass ctClass = ClassPool.getDefault().get(className);
                    CtMethod ctMethod = ctClass.getDeclaredMethod("main");
                    if (ctMethod ==null) {
                        System.out.println("ctMethod is null!");
                    }
                    ctMethod.insertAfter("System.out.println(111);");
                    return ctClass.toBytecode();
                } catch (NotFoundException e) {
                    e.printStackTrace();
                    System.out.println(e.getCause());
                } catch (CannotCompileException e) {
                    e.printStackTrace();
                    System.out.println(e.getCause());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if ("com.hzx.data.controller.TutorialController".equals(className)) {
                CtClass ctClass = null;
                try {
                    ctClass = ClassPool.getDefault().get(className);
                    CtMethod ctMethod = ctClass.getDeclaredMethod("getEmployee");
                    if (ctMethod ==null) {
                        System.out.println("ctMethod is null!");
                    }
                    ctMethod.insertAfter("System.out.println(222);");
                    ctMethod.insertBefore("System.out.println(\"哈哈哈\");");
                    return ctClass.toBytecode();
                } catch (NotFoundException e) {
                    e.printStackTrace();
                    System.out.println(e.getCause());
                } catch (CannotCompileException e) {
                    e.printStackTrace();
                    System.out.println(e.getCause());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return classfileBuffer;
        }
    }

}
