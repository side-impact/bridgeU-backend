-- ================================
-- Test Data Generation
-- ================================

-- Insert test users
INSERT INTO users (email, name, password) VALUES
  ('john@example.com',  'admin',   '$2a$10$dummypasswordhash1'),
  ('sarah@test.com',    'Sarah Lee',  '$2a$10$dummypasswordhash2'),
  ('mike@student.com',  'Mike Park',  '$2a$10$dummypasswordhash3'),
  ('alice@university.edu','Alice Choi','$2a$10$dummypasswordhash4'),
  ('david@gmail.com',   'David Jung', '$2a$10$dummypasswordhash5'),
  ('emma@yahoo.com',    'Emma Seo',   '$2a$10$dummypasswordhash6'),
  ('chris@hotmail.com', 'Chris Yoon', '$2a$10$dummypasswordhash7'),
  ('lisa@naver.com',    'Lisa Han',   '$2a$10$dummypasswordhash8'),
  ('ryan@daum.net',     'Ryan Kang',  '$2a$10$dummypasswordhash9'),
  ('jenny@kakao.com',   'Jenny Oh',   '$2a$10$dummypasswordhash10')
ON CONFLICT (email) DO NOTHING;;

-- Insert test posts (50 posts, all in English)
INSERT INTO posts (author_id, title, content, tags, view_count, content_text, created_at) VALUES
  -- Daily life
  (6, 'First weekend in Seoul', 'Just arrived in Seoul and wondering where to spend my first weekend. Hongdae, Gangnam, or Myeongdong?', ARRAY[1, 4], 45, 'Just arrived in Seoul and wondering where to spend my first weekend. Hongdae, Gangnam, or Myeongdong?', now() - interval '25 hours'),
  (2, 'Best Korean BBQ near campus', 'Looking for authentic Korean BBQ restaurants within walking distance from the university. Any recommendations?', ARRAY[1, 3, 5], 78, 'Looking for authentic Korean BBQ restaurants within walking distance from the university. Any recommendations?', now() - interval '22 hours'),
  (3, 'Dorm life tips', 'Want to share useful tips I learned while living in the dorm. From using laundry rooms to ordering late-night snacks!', ARRAY[2, 4], 500, 'Want to share useful tips I learned while living in the dorm. From using laundry rooms to ordering late-night snacks!', now() - interval '20 hours'),
  (4, 'Part-time jobs for international students', 'Sharing information about part-time jobs that are friendly to international students in Seoul.', ARRAY[3, 6], 89, 'Sharing information about part-time jobs that are friendly to international students in Seoul.', now() - interval '18 hours'),
  (5, 'Best ways to study Korean', 'Let’s discuss effective ways to study Korean, especially to improve conversation skills.', ARRAY[2, 5], 67, 'Let’s discuss effective ways to study Korean, especially to improve conversation skills.', now() - interval '16 hours'),

  -- Campus life
  (6, 'Library seat booking tips', 'Is it hard to book seats in the library? Here are some tips that worked for me!', ARRAY[2, 4], 156, 'Is it hard to book seats in the library? Here are some tips that worked for me!', now() - interval '14 hours'),
  (7, 'Study group for midterms', 'Anyone interested in forming a study group for upcoming midterm exams? Engineering students preferred.', ARRAY[2, 5], 450, 'Anyone interested in forming a study group for upcoming midterm exams? Engineering students preferred.', now() - interval '12 hours'),
  (8, 'Join the school festival team', 'Looking for people to help prepare for next week’s school festival. Sound team especially needed!', ARRAY[2, 6], 134, 'Looking for people to help prepare for next week’s school festival. Sound team especially needed!', now() - interval '10 hours'),
  (9, 'Is campus gym membership worth it?', 'Thinking about getting a gym membership at the campus fitness center. Is it worth it?', ARRAY[2, 4], 76, 'Thinking about getting a gym membership at the campus fitness center. Is it worth it?', now() - interval '8 hours'),
  (10, 'Club recommendations', 'Looking for a student club to join this semester. Music, sports, or academic clubs are welcome!', ARRAY[2, 5], 198, 'Looking for a student club to join this semester. Music, sports, or academic clubs are welcome!', now() - interval '6 hours'),

  -- Jobs
  (5, 'Preparing for IT internships', 'Planning to apply for IT internships. What should I prepare? Any advice from experienced students?', ARRAY[3, 4], 245, 'Planning to apply for IT internships. What should I prepare? Any advice from experienced students?', now() - interval '4 hours'),
  (2, 'Resume review session', 'Free resume review session for international students this Friday. Native speakers will help improve your resume.', ARRAY[3, 6], 156, 'Free resume review session for international students this Friday. Native speakers will help improve your resume.', now() - interval '3 hours'),
  (3, 'Startup vs big company', 'Not sure whether to choose a startup or a big company after graduation. Pros and cons?', ARRAY[3, 5], 187, 'Not sure whether to choose a startup or a big company after graduation. Pros and cons?', now() - interval '2 hours'),
  (4, 'LinkedIn networking tips', 'How to use LinkedIn effectively for job searching in Korea? Share your strategies!', ARRAY[3, 4], 134, 'How to use LinkedIn effectively for job searching in Korea? Share your strategies!', now() - interval '1 hour'),
  (5, 'Job hunting preparation', 'Starting to prepare for job hunting next year. What are the unique points about finding jobs in Korea?', ARRAY[3, 5], 440, 'Starting to prepare for job hunting next year. What are the unique points about finding jobs in Korea?', now() - interval '30 minutes'),

  -- Food spots
  (6, 'Best restaurants near campus', 'Made a list of the best restaurants near our campus. Organized by price range!', ARRAY[4, 4], 312, 'Made a list of the best restaurants near our campus. Organized by price range!', now() - interval '1 day 2 hours'),
  (7, 'Halal restaurants in Gangnam', 'Looking for halal-certified restaurants near Gangnam Station. Preferably Korean or Middle Eastern food.', ARRAY[4, 5], 89, 'Looking for halal-certified restaurants near Gangnam Station. Preferably Korean or Middle Eastern food.', now() - interval '1 day 4 hours'),
  (8, 'Good places to eat alone', 'Any recommendations for restaurants where it’s comfortable to eat alone, especially at dinner time?', ARRAY[4, 6], 145, 'Any recommendations for restaurants where it’s comfortable to eat alone, especially at dinner time?', now() - interval '1 day 6 hours'),
  (9, 'Korean cooking classes', 'Want to learn how to cook Korean food. Any good beginner-friendly classes in Seoul?', ARRAY[4, 4], 167, 'Want to learn how to cook Korean food. Any good beginner-friendly classes in Seoul?', now() - interval '1 day 8 hours'),
  (10, 'Late-night delivery food', 'Sharing my favorite late-night delivery restaurants. Chicken, pizza, Chinese food, etc!', ARRAY[4, 5], 223, 'Sharing my favorite late-night delivery restaurants. Chicken, pizza, Chinese food, etc!', now() - interval '1 day 10 hours'),

  -- Transportation
  (1, 'Subway Line 2 delay notice', 'Subway Line 2 will be delayed today between 2PM and 4PM on some sections.', ARRAY[8], 567, 'Subway Line 2 will be delayed today between 2PM and 4PM on some sections.', now() - interval '1 day 12 hours'),
  (1, 'Airport bus schedule changes', 'Important notice: Airport bus route 6015 schedule will change starting next Monday.', ARRAY[8], 234, 'Important notice: Airport bus route 6015 schedule will change starting next Monday.', now() - interval '1 day 14 hours'),
  (1, 'Taxi surcharge hours updated', 'Taxi surcharge hours have been changed for late night. Please check the details.', ARRAY[8], 189, 'Taxi surcharge hours have been changed for late night. Please check the details.', now() - interval '1 day 16 hours'),

  -- Convenience store
  (1, '24-hour convenience stores near campus', 'Here are the 24-hour convenience stores around the campus.', ARRAY[9], 145, 'Here are the 24-hour convenience stores around the campus.', now() - interval '1 day 18 hours'),
  (1, 'New convenience store services', 'ATM and package delivery services are now available at campus convenience stores.', ARRAY[9], 98, 'ATM and package delivery services are now available at campus convenience stores.', now() - interval '1 day 20 hours'),

  -- Fraud alert
  (1, 'Scholarship scam alert', 'Beware of fake scholarship scams. Do not provide personal information.', ARRAY[10], 432, 'Beware of fake scholarship scams. Do not provide personal information.', now() - interval '1 day 22 hours'),
  (1, 'Phone scam warning', 'Scammers are calling students pretending to be immigration officers. Do not give personal info.', ARRAY[10], 276, 'Scammers are calling students pretending to be immigration officers. Do not give personal info.', now() - interval '2 days'),

  -- Various posts
  (8, 'Korean culture event', 'Special Korean culture event for international students. Try Hanbok, traditional food, and more!', ARRAY[2, 1, 6], 298, 'Special Korean culture event for international students. Try Hanbok, traditional food, and more!', now() - interval '2 days 2 hours'),
  (9, 'Language exchange meetup', 'Weekly language exchange every Saturday at 2PM. Korean-English, Korean-Japanese available.', ARRAY[1, 2, 5], 187, 'Weekly language exchange every Saturday at 2PM. Korean-English, Korean-Japanese available.', now() - interval '2 days 4 hours'),
  (10, 'Phone contract in Korea', 'What you need to know when signing a phone contract in Korea. Required documents, tips, etc.', ARRAY[1, 4], 156, 'What you need to know when signing a phone contract in Korea. Required documents, tips, etc.', now() - interval '2 days 6 hours'),

  (6, 'Useful subway apps', 'Here are the best subway apps in Seoul. Real-time arrival info, shortest route, etc.', ARRAY[7, 1], 234, 'Here are the best subway apps in Seoul. Real-time arrival info, shortest route, etc.', now() - interval '2 days 8 hours'),
  (2, 'Banking guide for international students', 'Complete guide to opening a bank account in Korea as an international student.', ARRAY[1, 5], 345, 'Complete guide to opening a bank account in Korea as an international student.', now() - interval '2 days 10 hours'),
  (3, 'Study cafe vs library', 'Which is better for studying, study cafes or libraries? Pros and cons.', ARRAY[2, 6], 189, 'Which is better for studying, study cafes or libraries? Pros and cons.', now() - interval '2 days 12 hours'),
  (4, 'Healthcare in Korea', 'Guide to using the Korean healthcare system. Insurance, hospitals, emergencies.', ARRAY[1, 4], 267, 'Guide to using the Korean healthcare system. Insurance, hospitals, emergencies.', now() - interval '2 days 14 hours'),
  (5, 'Tips for finding part-time jobs', 'Sharing tips on how to find part-time jobs in Korea, especially for Japanese speakers.', ARRAY[3, 5], 178, 'Sharing tips on how to find part-time jobs in Korea, especially for Japanese speakers.', now() - interval '2 days 16 hours'),

  (6, 'Learning Korean with dramas', 'Sharing how to effectively study Korean by watching dramas!', ARRAY[1, 2, 4], 298, 'Sharing how to effectively study Korean by watching dramas!', now() - interval '2 days 18 hours'),
  (7, 'Best hiking spots near Seoul', 'Top hiking trails accessible by public transport. Great for stress relief!', ARRAY[1, 5], 223, 'Top hiking trails accessible by public transport. Great for stress relief!', now() - interval '2 days 20 hours'),
  (8, 'WiFi connection tips', 'Is your campus WiFi unstable? Here are some tips to connect more reliably.', ARRAY[2, 6], 167, 'Is your campus WiFi unstable? Here are some tips to connect more reliably.', now() - interval '2 days 22 hours'),
  (9, 'Korean skincare basics', 'Beginner-friendly Korean skincare routine for students. Affordable products.', ARRAY[1, 4], 312, 'Beginner-friendly Korean skincare routine for students. Affordable products.', now() - interval '3 days'),
  (10, 'Seasons and clothing in Korea', 'Guide to clothing by season in Korea, especially how to survive the cold winter.', ARRAY[1, 5], 145, 'Guide to clothing by season in Korea, especially how to survive the cold winter.', now() - interval '3 days 2 hours'),

  (7, 'Online class tips', 'How to stay focused and take online classes effectively.', ARRAY[2, 4], 234, 'How to stay focused and take online classes effectively.', now() - interval '3 days 4 hours'),
  (2, 'Korean festivals and holidays', 'Guide to major Korean festivals and holidays. How to participate and what to expect.', ARRAY[1, 6], 189, 'Guide to major Korean festivals and holidays. How to participate and what to expect.', now() - interval '3 days 6 hours'),
  (3, 'Living with roommates', 'How to maintain a good relationship with your dorm roommates. Tips and stories.', ARRAY[2, 1, 5], 267, 'How to maintain a good relationship with your dorm roommates. Tips and stories.', now() - interval '3 days 8 hours'),
  (4, 'Photography spots around campus', 'Best spots around campus for taking beautiful photos. Great for Instagram!', ARRAY[1, 4], 198, 'Best spots around campus for taking beautiful photos. Great for Instagram!', now() - interval '3 days 10 hours'),
  (5, 'Transportation cards in Korea', 'Explaining the types of Korean transportation cards like T-money and how to use them.', ARRAY[7, 5], 178, 'Explaining the types of Korean transportation cards like T-money and how to use them.', now() - interval '3 days 12 hours'),

  (6, 'Challenge Korean spicy food', 'Step-by-step guide to adapting to Korean spiciness. Useful for international students.', ARRAY[4, 6], 345, 'Step-by-step guide to adapting to Korean spiciness. Useful for international students.', now() - interval '3 days 14 hours'),
  (7, 'Student discounts in Seoul', 'Comprehensive list of student discounts available in Seoul.', ARRAY[1, 4], 289, 'Comprehensive list of student discounts available in Seoul.', now() - interval '3 days 16 hours'),
  (8, 'Managing stress during exams', 'Psychology major shares tips on managing stress during exam season.', ARRAY[2, 5], 234, 'Psychology major shares tips on managing stress during exam season.', now() - interval '3 days 18 hours'),
  (9, 'Korean learning resources', 'Recommended books, apps, and websites to learn Korean. From beginner to advanced.', ARRAY[1, 6], 198, 'Recommended books, apps, and websites to learn Korean. From beginner to advanced.', now() - interval '3 days 20 hours'),
  (10, 'Finding housing in Korea', 'What to consider when searching for housing in Korea. Tips from real experiences.', ARRAY[1, 4], 267, 'What to consider when searching for housing in Korea. Tips from real experiences.', now() - interval '3 days 22 hours');;
