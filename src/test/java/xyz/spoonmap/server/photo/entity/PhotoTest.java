package xyz.spoonmap.server.photo.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class PhotoTest {

    @Test
    void 파일_이름_가져오기() {
        // given
        Photo photo = Photo.builder()
                           .originName("test image")
                           .url("https://test.com/photo/03c73b16-887d-4c98-9dc9-d558afcd60a5-cubrid.png")
                           .build();

        // when
        String fileName = photo.getS3Key();

        // then
        String expectedFileName = "03c73b16-887d-4c98-9dc9-d558afcd60a5-cubrid.png";
        assertThat(fileName).isEqualTo(expectedFileName);
    }
}
