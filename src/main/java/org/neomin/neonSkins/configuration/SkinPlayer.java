package org.neomin.neonSkins.configuration;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkinPlayer {
    private String skinId;
    private String texture;
    private String signature;

    public boolean isValid() {
        return (texture != null && !texture.isEmpty()) &&
                (signature != null && !signature.isEmpty());
    }
}