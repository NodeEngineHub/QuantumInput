# ProGuard configuration for Entropy UI build
-allowaccessmodification
-repackageclasses
-dontnote "**"

# If obfuscation broke, remove dontwarn to see the error
-dontwarn "**"

# Keep all classes and members (fields, methods, etc.) annotated with @DontObfuscate
-keep @ca.nodeengine.core.DontObfuscate class *
-keepclassmembers class * {
    @ca.nodeengine.core.DontObfuscate *;
}

# keep members (fields, methods, etc...) in classes, if they are annotated with @DontObfuscateClassMembers
-keepclassmembers @ca.nodeengine.core.DontObfuscateClassMembers @interface ** { *; }
-keepclassmembers @ca.nodeengine.core.DontObfuscateClassMembers class * { *; }
-keepclassmembers class * { @ca.nodeengine.core.DontObfuscateClassMembers *; }

-keepattributes Signature,*Annotation*,EnclosingMethod,InnerClasses,Exceptions,SourceFile,LineNumberTable,Record
-keep class module-info { *; }

# Ensure ServiceLoader files are updated to match obfuscated names
-adaptresourcefilecontents META-INF/services/*
