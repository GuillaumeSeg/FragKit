#version 300 es
precision highp float;

const vec2 vertices[4] = vec2[](
    vec2(-1.0, -1.0),
    vec2(1.0, -1.0),
    vec2(-1.0, 1.0),
    vec2(1.0, 1.0)
);

const vec2 texCoords[4] = vec2[](
    vec2(0.0, 0.0),
    vec2(1.0, 0.0),
    vec2(0.0, 1.0),
    vec2(1.0, 1.0)
);

out vec2 v_TexCoord;

void main() {
    gl_Position = vec4(vertices[gl_VertexID], 0.0, 1.0);
    v_TexCoord = texCoords[gl_VertexID];
}