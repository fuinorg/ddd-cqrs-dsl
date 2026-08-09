package org.fuin.dsl.ddd.gen.view;

/**
 * The forms one operation of a <code>view</code> is rendered in. All of them come from the same model
 * method, which is what keeps the signatures from drifting apart.
 */
public enum ViewMethodShape {

    /** Declaration in the framework-free service contract: <code>Optional&lt;X&gt; find(final Id id);</code>. */
    SERVICE_DECL,

    /** Generate-once stub in the class implementing the service contract - throws. */
    SERVICE_IMPL_STUB,

    /** Declaration in the REST contract: annotated, and wrapped in a <code>ResponseEntity</code> for Spring. */
    REST_DECL,

    /** Implementation in the generated REST class - forwards to the service and maps absence to a 404. */
    REST_DELEGATE,

    /** Implementation in the generated REST client - forwards to the REST contract and unwraps it. */
    REST_CLIENT_DELEGATE

}
