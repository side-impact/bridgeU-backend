-- ================================
-- Test Data Generation
-- ================================

-- Insert test users
INSERT INTO users (login_id, username, password, created_at) VALUES
  ('john@example.com', 'John Kim', '$2a$10$dummypasswordhash1', now() - interval '30 days'),
  ('sarah@test.com', 'Sarah Lee', '$2a$10$dummypasswordhash2', now() - interval '25 days'), 
  ('mike@student.com', 'Mike Park', '$2a$10$dummypasswordhash3', now() - interval '20 days'),
  ('alice@university.edu', 'Alice Choi', '$2a$10$dummypasswordhash4', now() - interval '15 days'),
  ('david@gmail.com', 'David Jung', '$2a$10$dummypasswordhash5', now() - interval '10 days'),
  ('emma@yahoo.com', 'Emma Seo', '$2a$10$dummypasswordhash6', now() - interval '8 days'),
  ('chris@hotmail.com', 'Chris Yoon', '$2a$10$dummypasswordhash7', now() - interval '5 days'),
  ('lisa@naver.com', 'Lisa Han', '$2a$10$dummypasswordhash8', now() - interval '3 days'),
  ('ryan@daum.net', 'Ryan Kang', '$2a$10$dummypasswordhash9', now() - interval '2 days'),
  ('jenny@kakao.com', 'Jenny Oh', '$2a$10$dummypasswordhash10', now() - interval '1 day')
ON CONFLICT (login_id) DO NOTHING;;

-- Insert test posts (50 posts, all in English)
INSERT INTO posts (author_id, lang, title, content, tags, view_count, content_text, created_at) VALUES
  -- Daily life
  (1, 'en', 'First weekend in Seoul', 'Just arrived in Seoul and wondering where to spend my first weekend. Hongdae, Gangnam, or Myeongdong?', ARRAY[1, 4], 45, 'Just arrived in Seoul and wondering where to spend my first weekend. Hongdae, Gangnam, or Myeongdong?', now() - interval '25 hours'),
  (2, 'en', 'Best Korean BBQ near campus', 'Looking for authentic Korean BBQ restaurants within walking distance from the university. Any recommendations?', ARRAY[1, 3, 5], 78, 'Looking for authentic Korean BBQ restaurants within walking distance from the university. Any recommendations?', now() - interval '22 hours'),
  (3, 'en', 'Dorm life tips', 'Want to share useful tips I learned while living in the dorm. From using laundry rooms to ordering late-night snacks!', ARRAY[2, 4], 500, 'Want to share useful tips I learned while living in the dorm. From using laundry rooms to ordering late-night snacks!', now() - interval '20 hours'),
  (4, 'en', 'Part-time jobs for international students', 'Sharing information about part-time jobs that are friendly to international students in Seoul.', ARRAY[3, 6], 89, 'Sharing information about part-time jobs that are friendly to international students in Seoul.', now() - interval '18 hours'),
  (5, 'en', 'Best ways to study Korean', 'Let’s discuss effective ways to study Korean, especially to improve conversation skills.', ARRAY[2, 5], 67, 'Let’s discuss effective ways to study Korean, especially to improve conversation skills.', now() - interval '16 hours'),

  -- Campus life
  (6, 'en', 'Library seat booking tips', 'Is it hard to book seats in the library? Here are some tips that worked for me!', ARRAY[2, 4], 156, 'Is it hard to book seats in the library? Here are some tips that worked for me!', now() - interval '14 hours'),
  (7, 'en', 'Study group for midterms', 'Anyone interested in forming a study group for upcoming midterm exams? Engineering students preferred.', ARRAY[2, 5], 450, 'Anyone interested in forming a study group for upcoming midterm exams? Engineering students preferred.', now() - interval '12 hours'),
  (8, 'en', 'Join the school festival team', 'Looking for people to help prepare for next week’s school festival. Sound team especially needed!', ARRAY[2, 6], 134, 'Looking for people to help prepare for next week’s school festival. Sound team especially needed!', now() - interval '10 hours'),
  (9, 'en', 'Is campus gym membership worth it?', 'Thinking about getting a gym membership at the campus fitness center. Is it worth it?', ARRAY[2, 4], 76, 'Thinking about getting a gym membership at the campus fitness center. Is it worth it?', now() - interval '8 hours'),
  (10, 'en', 'Club recommendations', 'Looking for a student club to join this semester. Music, sports, or academic clubs are welcome!', ARRAY[2, 5], 198, 'Looking for a student club to join this semester. Music, sports, or academic clubs are welcome!', now() - interval '6 hours'),

  -- Jobs
  (1, 'en', 'Preparing for IT internships', 'Planning to apply for IT internships. What should I prepare? Any advice from experienced students?', ARRAY[3, 4], 245, 'Planning to apply for IT internships. What should I prepare? Any advice from experienced students?', now() - interval '4 hours'),
  (2, 'en', 'Resume review session', 'Free resume review session for international students this Friday. Native speakers will help improve your resume.', ARRAY[3, 6], 156, 'Free resume review session for international students this Friday. Native speakers will help improve your resume.', now() - interval '3 hours'),
  (3, 'en', 'Startup vs big company', 'Not sure whether to choose a startup or a big company after graduation. Pros and cons?', ARRAY[3, 5], 187, 'Not sure whether to choose a startup or a big company after graduation. Pros and cons?', now() - interval '2 hours'),
  (4, 'en', 'LinkedIn networking tips', 'How to use LinkedIn effectively for job searching in Korea? Share your strategies!', ARRAY[3, 4], 134, 'How to use LinkedIn effectively for job searching in Korea? Share your strategies!', now() - interval '1 hour'),
  (5, 'en', 'Job hunting preparation', 'Starting to prepare for job hunting next year. What are the unique points about finding jobs in Korea?', ARRAY[3, 5], 440, 'Starting to prepare for job hunting next year. What are the unique points about finding jobs in Korea?', now() - interval '30 minutes'),

  -- Food spots
  (6, 'en', 'Best restaurants near campus', 'Made a list of the best restaurants near our campus. Organized by price range!', ARRAY[4, 4], 312, 'Made a list of the best restaurants near our campus. Organized by price range!', now() - interval '1 day 2 hours'),
  (7, 'en', 'Halal restaurants in Gangnam', 'Looking for halal-certified restaurants near Gangnam Station. Preferably Korean or Middle Eastern food.', ARRAY[4, 5], 89, 'Looking for halal-certified restaurants near Gangnam Station. Preferably Korean or Middle Eastern food.', now() - interval '1 day 4 hours'),
  (8, 'en', 'Good places to eat alone', 'Any recommendations for restaurants where it’s comfortable to eat alone, especially at dinner time?', ARRAY[4, 6], 145, 'Any recommendations for restaurants where it’s comfortable to eat alone, especially at dinner time?', now() - interval '1 day 6 hours'),
  (9, 'en', 'Korean cooking classes', 'Want to learn how to cook Korean food. Any good beginner-friendly classes in Seoul?', ARRAY[4, 4], 167, 'Want to learn how to cook Korean food. Any good beginner-friendly classes in Seoul?', now() - interval '1 day 8 hours'),
  (10, 'en', 'Late-night delivery food', 'Sharing my favorite late-night delivery restaurants. Chicken, pizza, Chinese food, etc!', ARRAY[4, 5], 223, 'Sharing my favorite late-night delivery restaurants. Chicken, pizza, Chinese food, etc!', now() - interval '1 day 10 hours'),

  -- Transportation
  (1, 'en', 'Subway Line 2 delay notice', 'Subway Line 2 will be delayed today between 2PM and 4PM on some sections.', ARRAY[8], 567, 'Subway Line 2 will be delayed today between 2PM and 4PM on some sections.', now() - interval '1 day 12 hours'),
  (2, 'en', 'Airport bus schedule changes', 'Important notice: Airport bus route 6015 schedule will change starting next Monday.', ARRAY[8], 234, 'Important notice: Airport bus route 6015 schedule will change starting next Monday.', now() - interval '1 day 14 hours'),
  (3, 'en', 'Taxi surcharge hours updated', 'Taxi surcharge hours have been changed for late night. Please check the details.', ARRAY[8], 189, 'Taxi surcharge hours have been changed for late night. Please check the details.', now() - interval '1 day 16 hours'),

  -- Convenience store
  (4, 'en', '24-hour convenience stores near campus', 'Here are the 24-hour convenience stores around the campus.', ARRAY[9], 145, 'Here are the 24-hour convenience stores around the campus.', now() - interval '1 day 18 hours'),
  (5, 'en', 'New convenience store services', 'ATM and package delivery services are now available at campus convenience stores.', ARRAY[9], 98, 'ATM and package delivery services are now available at campus convenience stores.', now() - interval '1 day 20 hours'),

  -- Fraud alert
  (6, 'en', 'Scholarship scam alert', 'Beware of fake scholarship scams. Do not provide personal information.', ARRAY[10], 432, 'Beware of fake scholarship scams. Do not provide personal information.', now() - interval '1 day 22 hours'),
  (7, 'en', 'Phone scam warning', 'Scammers are calling students pretending to be immigration officers. Do not give personal info.', ARRAY[10], 276, 'Scammers are calling students pretending to be immigration officers. Do not give personal info.', now() - interval '2 days'),

  -- Various posts
  (8, 'en', 'Korean culture event', 'Special Korean culture event for international students. Try Hanbok, traditional food, and more!', ARRAY[2, 1, 6], 298, 'Special Korean culture event for international students. Try Hanbok, traditional food, and more!', now() - interval '2 days 2 hours'),
  (9, 'en', 'Language exchange meetup', 'Weekly language exchange every Saturday at 2PM. Korean-English, Korean-Japanese available.', ARRAY[1, 2, 5], 187, 'Weekly language exchange every Saturday at 2PM. Korean-English, Korean-Japanese available.', now() - interval '2 days 4 hours'),
  (10, 'en', 'Phone contract in Korea', 'What you need to know when signing a phone contract in Korea. Required documents, tips, etc.', ARRAY[1, 4], 156, 'What you need to know when signing a phone contract in Korea. Required documents, tips, etc.', now() - interval '2 days 6 hours'),

  (1, 'en', 'Useful subway apps', 'Here are the best subway apps in Seoul. Real-time arrival info, shortest route, etc.', ARRAY[7, 1], 234, 'Here are the best subway apps in Seoul. Real-time arrival info, shortest route, etc.', now() - interval '2 days 8 hours'),
  (2, 'en', 'Banking guide for international students', 'Complete guide to opening a bank account in Korea as an international student.', ARRAY[1, 5], 345, 'Complete guide to opening a bank account in Korea as an international student.', now() - interval '2 days 10 hours'),
  (3, 'en', 'Study cafe vs library', 'Which is better for studying, study cafes or libraries? Pros and cons.', ARRAY[2, 6], 189, 'Which is better for studying, study cafes or libraries? Pros and cons.', now() - interval '2 days 12 hours'),
  (4, 'en', 'Healthcare in Korea', 'Guide to using the Korean healthcare system. Insurance, hospitals, emergencies.', ARRAY[1, 4], 267, 'Guide to using the Korean healthcare system. Insurance, hospitals, emergencies.', now() - interval '2 days 14 hours'),
  (5, 'en', 'Tips for finding part-time jobs', 'Sharing tips on how to find part-time jobs in Korea, especially for Japanese speakers.', ARRAY[3, 5], 178, 'Sharing tips on how to find part-time jobs in Korea, especially for Japanese speakers.', now() - interval '2 days 16 hours'),

  (6, 'en', 'Learning Korean with dramas', 'Sharing how to effectively study Korean by watching dramas!', ARRAY[1, 2, 4], 298, 'Sharing how to effectively study Korean by watching dramas!', now() - interval '2 days 18 hours'),
  (7, 'en', 'Best hiking spots near Seoul', 'Top hiking trails accessible by public transport. Great for stress relief!', ARRAY[1, 5], 223, 'Top hiking trails accessible by public transport. Great for stress relief!', now() - interval '2 days 20 hours'),
  (8, 'en', 'WiFi connection tips', 'Is your campus WiFi unstable? Here are some tips to connect more reliably.', ARRAY[2, 6], 167, 'Is your campus WiFi unstable? Here are some tips to connect more reliably.', now() - interval '2 days 22 hours'),
  (9, 'en', 'Korean skincare basics', 'Beginner-friendly Korean skincare routine for students. Affordable products.', ARRAY[1, 4], 312, 'Beginner-friendly Korean skincare routine for students. Affordable products.', now() - interval '3 days'),
  (10, 'en', 'Seasons and clothing in Korea', 'Guide to clothing by season in Korea, especially how to survive the cold winter.', ARRAY[1, 5], 145, 'Guide to clothing by season in Korea, especially how to survive the cold winter.', now() - interval '3 days 2 hours'),

  (1, 'en', 'Online class tips', 'How to stay focused and take online classes effectively.', ARRAY[2, 4], 234, 'How to stay focused and take online classes effectively.', now() - interval '3 days 4 hours'),
  (2, 'en', 'Korean festivals and holidays', 'Guide to major Korean festivals and holidays. How to participate and what to expect.', ARRAY[1, 6], 189, 'Guide to major Korean festivals and holidays. How to participate and what to expect.', now() - interval '3 days 6 hours'),
  (3, 'en', 'Living with roommates', 'How to maintain a good relationship with your dorm roommates. Tips and stories.', ARRAY[2, 1, 5], 267, 'How to maintain a good relationship with your dorm roommates. Tips and stories.', now() - interval '3 days 8 hours'),
  (4, 'en', 'Photography spots around campus', 'Best spots around campus for taking beautiful photos. Great for Instagram!', ARRAY[1, 4], 198, 'Best spots around campus for taking beautiful photos. Great for Instagram!', now() - interval '3 days 10 hours'),
  (5, 'en', 'Transportation cards in Korea', 'Explaining the types of Korean transportation cards like T-money and how to use them.', ARRAY[7, 5], 178, 'Explaining the types of Korean transportation cards like T-money and how to use them.', now() - interval '3 days 12 hours'),

  (6, 'en', 'Challenge Korean spicy food', 'Step-by-step guide to adapting to Korean spiciness. Useful for international students.', ARRAY[4, 6], 345, 'Step-by-step guide to adapting to Korean spiciness. Useful for international students.', now() - interval '3 days 14 hours'),
  (7, 'en', 'Student discounts in Seoul', 'Comprehensive list of student discounts available in Seoul.', ARRAY[1, 4], 289, 'Comprehensive list of student discounts available in Seoul.', now() - interval '3 days 16 hours'),
  (8, 'en', 'Managing stress during exams', 'Psychology major shares tips on managing stress during exam season.', ARRAY[2, 5], 234, 'Psychology major shares tips on managing stress during exam season.', now() - interval '3 days 18 hours'),
  (9, 'en', 'Korean learning resources', 'Recommended books, apps, and websites to learn Korean. From beginner to advanced.', ARRAY[1, 6], 198, 'Recommended books, apps, and websites to learn Korean. From beginner to advanced.', now() - interval '3 days 20 hours'),
  (10, 'en', 'Finding housing in Korea', 'What to consider when searching for housing in Korea. Tips from real experiences.', ARRAY[1, 4], 267, 'What to consider when searching for housing in Korea. Tips from real experiences.', now() - interval '3 days 22 hours');;

-- Insert image blocks for some posts
INSERT INTO post_blocks (post_id, idx, type, image_url) VALUES
  (1, 0, 'image', 'https://example.com/images/seoul_weekend_1.jpg'),
  (6, 0, 'image', 'https://example.com/images/library_tips.jpg'),
  (16, 0, 'image', 'https://example.com/images/korean_bbq.jpg'),
  (26, 0, 'image', 'https://example.com/images/culture_event.jpg'),
  (36, 0, 'image', 'https://example.com/images/hiking_spot.jpg'),
  (46, 0, 'image', 'https://example.com/images/photography_campus.jpg')
ON CONFLICT (post_id, idx) DO NOTHING;;
