import org.objectweb.asm.Opcodes;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.util.Map;
import java.util.HashMap;

/**
 * Special class loader, which when running on Sun VM allows to generate accessor classes for any method
 */
public class SunClassLoader extends ClassLoader implements Opcodes {

    private void loadMagic() {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cw.visit(Opcodes.V1_4, Opcodes.ACC_PUBLIC, "sun/reflect/GroovyMagic", null, "sun/reflect/MagicAccessorImpl", null);
        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, "sun/reflect/MagicAccessorImpl", "<init>", "()V", false);
        mv.visitInsn(RETURN);
        mv.visitMaxs(0,0);
        mv.visitEnd();
        cw.visitEnd();

        define(cw.toByteArray(), "sun.reflect.GroovyMagic");
    }

    protected void define(byte[] bytes, final String name) {
        //knownClasses.put(name, defineClass(name, bytes, 0, bytes.length));
        Class cls = defineClass(name, bytes, 0, bytes.length);
        //Class cls2 = defineClass(name, bytes, 0, bytes.length);
    }

    protected final Map<String,Class> knownClasses = new HashMap<String,Class>();

    public static void main(String[] args) throws Exception {
        SunClassLoader sun = new SunClassLoader();
        sun.loadMagic();
        
        Class cls2 = sun.loadClass("java/lang/Object");
    }
}
