CREATE SCHEMA IF NOT EXISTS matches;
-- Create the Match table
CREATE TABLE matches.match (
    id uuid,
    description VARCHAR(255) NOT NULL,
    match_date DATE NOT NULL,
    match_time TIME NOT NULL,
    team_a VARCHAR(100) NOT NULL,
    team_b VARCHAR(100) NOT NULL,
    sport VARCHAR(50) NOT NULL
);

-- Add primary key constraint to Match
ALTER TABLE matches.match
    ADD CONSTRAINT match_pk PRIMARY KEY (id);

-- Create the MatchOdds table
CREATE TABLE matches.match_odds (
    id uuid,
    match_id uuid NOT NULL,
    specifier VARCHAR(50) NOT NULL,
    odd NUMERIC(5, 2) NOT NULL CHECK (odd > 0),
    FOREIGN KEY (match_id) REFERENCES matches.match (id) ON DELETE CASCADE
);


ALTER TABLE matches.match_odds
    ADD CONSTRAINT matchodds_pk PRIMARY KEY (id);

INSERT INTO matches.match (id, description, match_date, match_time, team_a, team_b, sport)
VALUES
    ('7e0d7c90-e304-4085-b6cc-0c497c16d4b9', 'Team A vs Team B', '2024-11-16', '19:00:00', 'Team A', 'Team B', 'Football'),
    ('7e0d7c90-e304-4085-b6cc-0c497c16d4b8', 'Team C vs Team D', '2024-11-17', '21:00:00', 'Team C', 'Team D', 'Basketball');

-- Insert sample data into MatchOdds
INSERT INTO matches.match_odds (id, match_id, specifier, odd)
VALUES
    ('10e92e20-76bf-40d7-ae5f-abcdefabcdef', '7e0d7c90-e304-4085-b6cc-0c497c16d4b9', 'Win', 1.50),
    ('20f13d30-87cf-51e8-be6f-bcdefabcdef0', '7e0d7c90-e304-4085-b6cc-0c497c16d4b9', 'Draw', 3.20),
    ('30a24e40-98df-62f9-cf7f-cdefabcdef01', '7e0d7c90-e304-4085-b6cc-0c497c16d4b9', 'Lose', 2.70),
    ('40b35f50-a9ef-73f7-df8f-defabcdef012', '7e0d7c90-e304-4085-b6cc-0c497c16d4b8', 'Win', 1.75),
    ('7e0d7c90-e304-4085-b6cc-0c497c16d4b5', '7e0d7c90-e304-4085-b6cc-0c497c16d4b8', 'Lose', 2.10);
