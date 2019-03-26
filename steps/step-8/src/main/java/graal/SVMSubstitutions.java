package graal;

import com.oracle.svm.core.annotate.*;
import org.graalvm.nativeimage.*;

import io.vertx.core.Vertx;
import io.vertx.core.dns.AddressResolverOptions;
import io.vertx.core.impl.resolver.DefaultResolverProvider;
import io.vertx.core.spi.resolver.ResolverProvider;

/**
 * This substitution allows the usage of platform specific code to do low level
 * buffer related tasks
 */
@TargetClass(className = "io.netty.util.internal.CleanerJava6")
final class Target_io_netty_util_internal_CleanerJava6 {

    @Alias
    @RecomputeFieldValue(kind = RecomputeFieldValue.Kind.FieldOffset, declClassName = "java.nio.DirectByteBuffer", name = "cleaner")
    private static long CLEANER_FIELD_OFFSET;
}

/**
 * This substitution allows the usage of platform specific code to do low level
 * buffer related tasks
 */
@TargetClass(className = "io.netty.util.internal.PlatformDependent0")
final class Target_io_netty_util_internal_PlatformDependent0 {

    @Alias
    @RecomputeFieldValue(kind = RecomputeFieldValue.Kind.FieldOffset, declClassName = "java.nio.Buffer", name = "address")
    private static long ADDRESS_FIELD_OFFSET;
}

/**
 * This substitution allows the usage of platform specific code to do low level
 * buffer related tasks
 */
@TargetClass(className = "io.netty.util.internal.shaded.org.jctools.util.UnsafeRefArrayAccess")
final class Target_io_netty_util_internal_shaded_org_jctools_util_UnsafeRefArrayAccess {
    private Target_io_netty_util_internal_shaded_org_jctools_util_UnsafeRefArrayAccess() {}
    @Alias
    @RecomputeFieldValue(kind = RecomputeFieldValue.Kind.ArrayIndexShift, declClass = Object[].class)
    public static int REF_ELEMENT_SHIFT;
}

/**
 * This substitution forces the usage of the blocking DNS resolver
 */
@TargetClass(className = "io.vertx.core.spi.resolver.ResolverProvider")
final class TargetResolverProvider {

    @Substitute
    public static ResolverProvider factory(Vertx vertx, AddressResolverOptions options) {
        return new DefaultResolverProvider();
    }
}
