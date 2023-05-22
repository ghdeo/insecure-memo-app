from flask import Flask, request
import sqlite3

app = Flask(__name__)
DATABASE = 'database.db'

def initialize_db():
    conn = sqlite3.connect(DATABASE)
    cursor = conn.cursor()
    cursor.execute('CREATE TABLE IF NOT EXISTS memos (id INTEGER PRIMARY KEY, category TEXT, content TEXT)')
    conn.commit()
    conn.close()

@app.route('/api/memo', methods=['POST'])
def handle_post_request():
    category = request.form.get('category') # POST 요청에서 'category'라는 필드의 값을 가져옴
    content = request.form.get('content')  # POST 요청에서 'content'라는 필드의 값을 가져옴

    # SQL 쿼리 생성 (취약점 존재)
    # 취약점: 사용자 입력을 직접 쿼리에 포함시킴
    script = f'''
        INSERT INTO memos (category, content) VALUES ('{category}', '{content}');
    '''

    conn = sqlite3.connect(DATABASE)
    cursor = conn.cursor()
    cursor.executescript(script)

    result = cursor.fetchall()
    conn.commit()
    conn.close()

    return 'POST 요청이 성공적으로 처리되었습니다.'

if __name__ == '__main__':
    initialize_db()
    app.run(debug=True)
