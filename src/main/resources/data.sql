
INSERT INTO universities (code, labels) VALUES
  ('SNU', '{"en":"Seoul National University","ja":"ソウル大学","zh-CN":"首尔大学"}'::jsonb),
  ('KU',  '{"en":"Korea University","ja":"高麗大学","zh-CN":"高丽大学"}'::jsonb),
  ('YU',  '{"en":"Yonsei University","ja":"延世大学","zh-CN":"延世大学"}'::jsonb)
ON CONFLICT (code) DO NOTHING;;

INSERT INTO tags (slug, labels, "group", is_user_selectable) VALUES
  ('daily_life',  '{"en":"Daily Life","ja":"日常生活","zh-CN":"日常生活"}'::jsonb, 'user',  true),
  ('campus_life', '{"en":"Campus Life","ja":"キャンパスライフ","zh-CN":"校园生活"}'::jsonb, 'user',  true),
  ('jobs',        '{"en":"Jobs","ja":"仕事","zh-CN":"工作"}'::jsonb,               'user',  true),
  ('food_spots',  '{"en":"Food Spots","ja":"グルメ","zh-CN":"美食"}'::jsonb,       'user',  true)
ON CONFLICT (slug) DO NOTHING;;

INSERT INTO tags (slug, labels, "group", is_user_selectable) VALUES
  ('school_snu', '{"en":"SNU","ja":"SNU","zh-CN":"SNU"}'::jsonb, 'school', true),
  ('school_ku',  '{"en":"KU","ja":"KU","zh-CN":"KU"}'::jsonb,    'school', true),
  ('school_yu',  '{"en":"YU","ja":"YU","zh-CN":"YU"}'::jsonb,    'school', true)
ON CONFLICT (slug) DO NOTHING;;

INSERT INTO tags (slug, labels, "group", is_user_selectable) VALUES
  ('transportation',    '{"en":"Transportation","ja":"交通","zh-CN":"交通"}'::jsonb,                 'notice', false),
  ('convenience_store', '{"en":"Convenience Store","ja":"コンビニ","zh-CN":"便利店"}'::jsonb,         'notice', false),
  ('fraud_alert',       '{"en":"Fraud Alert","ja":"詐欺注意","zh-CN":"防诈骗"}'::jsonb,               'notice', false)
ON CONFLICT (slug) DO NOTHING;;