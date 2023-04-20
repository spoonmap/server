package xyz.spoonmap.server.notification.repository;

// @DataJpaTest
// @Import(QueryDslConfig.class)
// class NotificationRepositoryTest {
//
// @Autowired
// NotificationRepository notificationRepository;
//
// @Autowired
// MemberRepository memberRepository;
//
// Random random = new Random();
//
// @Test
// @DisplayName("알림 조회")
// void testFindNotifications() {
//     PasswordEncoder encoder = new BCryptPasswordEncoder();
//     Member member = new Member("김철수", "asdffsd@email.com", encoder.encode("password"), "철수", null);
//     memberRepository.save(member);
//
//     int size = random.nextInt(100);
//
//     List<Notification> notifications = notificationRepository.saveAll(createNotification(size, member));
//     Notification notification = notifications.get(notifications.size() - 1);
//     Slice<NotificationResponse> notifications1 =
//         notificationRepository.findNotifications(member.getId(), notification.getId(), 10);
//     assertThat(notifications1.hasNext()).isFalse();
// }
//
// private List<Notification> createNotification(int size, Member member) {
//     return IntStream.range(0, size)
//                     .mapToObj(
//                         i -> new Notification(member, selectType(random.nextInt(size)), random.nextLong(size)))
//                     .toList();
// }
//
// private NotificationType selectType(int n) {
//     return n % 2 == 0 ? NotificationType.COMMENT : NotificationType.FOLLOW;
// }
//
// }
