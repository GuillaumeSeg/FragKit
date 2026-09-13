// shaders/plasma.frag
precision highp float;
uniform float u_time;
uniform vec2 u_resolution;

void main() {
    vec2 uv = gl_FragCoord.xy / u_resolution;
    vec2 p = (uv - 0.5) * 2.0;

    float v = 0.0;
    v += sin(p.x * 4.0 + u_time);
    v += sin(p.y * 4.0 + u_time * 1.3);
    v += sin((p.x + p.y) * 4.0 + u_time * 0.7);
    v += sin(length(p) * 6.0 - u_time * 1.5);

    vec3 color = vec3(
        0.5 + 0.5 * sin(v * 3.0 + 0.0),
        0.5 + 0.5 * sin(v * 3.0 + 2.09),
        0.5 + 0.5 * sin(v * 3.0 + 4.18)
    );

    gl_FragColor = vec4(color, 1.0);
}