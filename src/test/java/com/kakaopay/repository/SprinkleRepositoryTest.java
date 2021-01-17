package com.kakaopay.repository;

import com.kakaopay.constant.Constant;
import com.kakaopay.entity.Sprinkle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SprinkleRepositoryTest {

    @Autowired
    private SprinkleRepository sprinkleRepository;

    @BeforeEach
    void setup() {
        sprinkleRepository.deleteAllInBatch();
    }

    @Test
    void existsByTokenAndUserIdAndCreatedAtAfter_생성하고바로_가져왔으므로_참() {
        // given
        Sprinkle sprinkle1 = Sprinkle.builder().amount(1000).roomId("abcdeABCDE").userId("001").token("ABC").build();
        sprinkleRepository.save(sprinkle1);
        // when
        Boolean result = sprinkleRepository.existsByTokenAndUserIdAndCreatedAtAfter("ABC", "001", LocalDateTime.now().minusDays(Constant.SPRINKLE_LOOKUP_MAX_DAY));
        // assert
        assertTrue(result);
    }

    @Test
    void existsByTokenAndUserIdAndCreatedAtAfter_전혀_해당하는데이터가_없으므로_거짓() {
        // when
        Boolean result = sprinkleRepository.existsByTokenAndUserIdAndCreatedAtAfter("ABC", "001", LocalDateTime.now().minusDays(Constant.SPRINKLE_LOOKUP_MAX_DAY));
        // assert
        assertFalse(result);
    }

    @Test
    void existsByTokenAndUserIdAndCreatedAtAfter_같은_토큰은있지만_유저아이디가_다르므로_거짓() {
        // given
        List<Sprinkle> sprinkles = new ArrayList<>();
        sprinkles.add(Sprinkle.builder().amount(1000).roomId("abcdeABCDE").userId("001").token("ABC").build());
        sprinkles.add(Sprinkle.builder().amount(1000).roomId("abcdeABCDE").userId("002").token("ABC").build());
        sprinkleRepository.saveAll(sprinkles);

        // when
        Boolean result = sprinkleRepository.existsByTokenAndUserIdAndCreatedAtAfter("ABC", "003", LocalDateTime.now().minusDays(Constant.SPRINKLE_LOOKUP_MAX_DAY));

        // assert
        assertFalse(result);
    }

    @Test
    void findByTokenAndCreatedAtAfter_내가생성한_뿌리기가_있을때() {
        // given
        Sprinkle sprinkle = Sprinkle.builder().amount(1000).roomId("abcdeABCDE").userId("001").token("ABC").build();
        sprinkleRepository.save(sprinkle);

        // when
        Optional<Sprinkle> sprinkle1 = sprinkleRepository.findTop1ByTokenAndUserIdAndCreatedAtAfter("ABC", "001", LocalDateTime.now().minusDays(Constant.SPRINKLE_LOOKUP_MAX_DAY));

        // assert
        assertThat(sprinkle1).isPresent();
    }

    @Test
    void findByTokenAndCreatedAtAfter_내가생성한_뿌리기가_없을때() {
        // given
        Sprinkle sprinkle = Sprinkle.builder().amount(1000).roomId("abcdeABCDE").userId("001").token("ABC").build();
        sprinkleRepository.save(sprinkle);

        // when
        Optional<Sprinkle> sprinkle1 = sprinkleRepository.findTop1ByTokenAndUserIdAndCreatedAtAfter("ABC", "002", LocalDateTime.now().minusDays(Constant.SPRINKLE_LOOKUP_MAX_DAY));

        // assert
        assertThat(sprinkle1).isEmpty();
    }

    @Test
    void findByTokenAndCreatedAtAfter_토큰이_일치하지_않을때() {
        // given
        List<Sprinkle> sprinkles = new ArrayList<>();
        sprinkles.add(Sprinkle.builder().amount(1000).roomId("abcdeABCDE").userId("001").token("ABC").build());
        sprinkles.add(Sprinkle.builder().amount(1000).roomId("abcdeABCDE").userId("001").token("aBc").build());
        sprinkles.add(Sprinkle.builder().amount(1000).roomId("abcdeABCDE").userId("001").token("ABc").build());
        sprinkleRepository.saveAll(sprinkles);

        // when
        Optional<Sprinkle> sprinkle1 = sprinkleRepository.findTop1ByTokenAndUserIdAndCreatedAtAfter("abc", "001", LocalDateTime.now().minusDays(Constant.SPRINKLE_LOOKUP_MAX_DAY));

        // assert
        assertThat(sprinkle1).isEmpty();
    }

    @Test
    void findTop1ByTokenAndRoomIdAndCreatedAtAfter_정보가있을때() {
        // given
        Sprinkle sprinkle = Sprinkle.builder().amount(1000).roomId("abcdeABCDE").userId("001").token("ABC").build();
        sprinkleRepository.save(sprinkle);

        // when
        Optional<Sprinkle> sprinkle1 = sprinkleRepository.findTop1ByTokenAndRoomIdAndCreatedAtAfter("ABC", "abcdeABCDE", LocalDateTime.now().minusDays(Constant.SPRINKLE_LOOKUP_MAX_DAY));

        // assert
        assertThat(sprinkle1).isPresent();


        // given
        sprinkleRepository.save(Sprinkle.builder().amount(1000).roomId("abcdeABCDE").userId("001").token("ABc").build());

        // when
        Optional<Sprinkle> sprinkle3 = sprinkleRepository.findTop1ByTokenAndRoomIdAndCreatedAtAfter("ABc", "abcdeABCDE", LocalDateTime.now().minusDays(Constant.SPRINKLE_LOOKUP_MAX_DAY));

        // assert
        assertThat(sprinkle3).isPresent();
    }

    @Test
    void findTop1ByTokenAndRoomIdAndCreatedAtAfter_정보가_없을떄() {
        // given
        List<Sprinkle> sprinkles = new ArrayList<>();
        sprinkles.add(Sprinkle.builder().amount(1000).roomId("abcdeABCDE").userId("001").token("ABC").build());
        sprinkles.add(Sprinkle.builder().amount(1000).roomId("abcdeABCDE").userId("002").token("ABC").build());
        sprinkleRepository.saveAll(sprinkles);

        Optional<Sprinkle> sprinkle3 = sprinkleRepository.findTop1ByTokenAndRoomIdAndCreatedAtAfter("ABC", "abcdeabcde", LocalDateTime.now().minusDays(Constant.SPRINKLE_LOOKUP_MAX_DAY));

        // assert
        assertThat(sprinkle3).isEmpty();
    }
}