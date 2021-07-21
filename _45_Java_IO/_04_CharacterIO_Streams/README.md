# HƯỚNG DẪN SỬ DỤNG CHARACTER I/O STREAM
__________________________________________________________________________________________________________________________________________________________________________________
## GIỚI THIỆU
* trong **Binary I/O Stream** (Luồng vào ra nhị phân)
    * mỗi lần **write/read** là **1 byte** (tương đương với 8 bit nhị phân)

* trong **Character I/O Stream** (Luồng vào ra ký tự)
    * tùy thuộc vào kiểu mã hóa **(encoding)** **UTF-8, UTF-16, ...** dẫn đến mỗi lần **write/read** là 1, 2, hoặc 3 bytes

* trong Java có nhiều class hỗ trợ các thao tác với **Character I/O Stream**, dẫn đầu là 2 class **Reader** & **Writer**
    * **java.io.Writer**: được sử dụng để ghi dữ liệu đến đích (destination)
    * **java.io.Reader**: được sử dụng để đọc dữ liệu từ một nguồn (source)

![img.png](img.png)

__________________________________________________________________________________________________________________________________________________________________________________

## NỘI DUNG

1. [Writer](#1)
2. [Reader](#2)
3. [OutputStreamWriter](#3)
4. [InputStreamReader](#4)
5. [FileWriter](#5)
6. [FileReader](#6)
7. [CharArrayWriter](#7)
8. [CharArrayReader](#8)
9. [FilterWriter](#9)
10. [FilterReader](#10)
11. [PushbackReader](#11)
12. [BufferedWriter](#12)
13. [BufferedReader](#13)
14. [LineNumberReader](#14)
15. [StringWriter](#15)
16. [StringReader](#16)
17. [PipedWriter](#17)
18. [PipedReader](#18)
19. [LineNumberReader](#19)
20. [PrintWriter](#20)
21. [PrintStream](#21)

__________________________________________________________________________________________________________________________________________________________________________________

## 1. Writer <a id="1"></a>
* **Writer** là 1 **abstract class** nằm trong package **java.io**
```java
public abstract class Writer extends Object implements Appendable, Closeable, Flushable {
    // ...
}
```
* **abstract class Writer** đại diện cho **character output stream** để **write** các **character** vào 1 mục tiêu nào đó, ví dụ như file văn bản

![img_1.png](img_1.png)

* ta không thể sử dụng trực tiếp **Writer** vì nó là 1 **abstract class**, tùy tình huống cụ thể mà ta có thể sử dụng các **subclass** của **Writer**
* các **subclass** của **Writer**
    * ``BufferedWriter``
    * ``CharArrayWriter``
    * ``FilterWriter``
    * ``OutputStreamWriter``
    * ``PipedWriter``
    * ``PrintWriter``
    * ``StringWriter``

* xét ví dụ ghi các ký tự sau vào 1 file với **UTF-8**

![img_3.png](img_3.png)

* Java sử dụng 2 **bytes** để lưu trữ 1 ký tự, và đây là hình minh họa của văn bản trên

![img_4.png](img_4.png)

* **FileWriter** là 1 **subclass** của **Writer** thường được sử dụng để ghi các ký tự vào 1 file văn bản
* mỗi ký tự trên **Writer** là 2 bytes
* nhưng khi ghi các ký tự vào 1 file văn bản với **UTF-8**, mỗi ký tự có thể được lưu trữ bởi 1, 2, 3 hoặc 4 bytes

![img_5.png](img_5.png)
__________________________________________________________________________________________________________________________________________________________________________________

### 1.1. Fields in Writer
```java
public abstract class Writer extends Object implements Appendable, Closeable, Flushable {
    // Field    
    protected Object lock;
    private char[] writeBuffer;
    private static final int WRITE_BUFFER_SIZE = 1024;
}
```
* ``lock`` là 1 object được sử dụng để **synchronize** các hoạt động trên **character output stream**
* For efficiency, a character-stream object may use an object other than itself to protect critical sections.
* A subclass should therefore use the object in this field rather than this or a synchronized method.
__________________________________________________________________________________________________________________________________________________________________________________

### 1.2. Constructors in Writer
```java
public abstract class Writer extends Object implements Appendable, Closeable, Flushable {
    // Constructor
    protected Writer() { // ... }
    protected Writer(Object lock) { // ... }
        }
```
* **protected Writer()**
    * _tạo 1 character stream writer có các phần quan trọng sẽ synchronize trên chính writer_<br/><br/>
* **protected Writer(Object lock)**
    * _tạo 1 character stream writer có các phần quan trọng sẽ synchronize trên chính object chỉ định_
__________________________________________________________________________________________________________________________________________________________________________________

### 1.3. Methods in Writer
```java
public abstract class Writer extends Object implements Appendable, Closeable, Flushable {
    // Methods
    public abstract void write(char cbuf[], int off, int len) throws IOException;
    public abstract void flush() throws IOException;
    public abstract void close() throws IOException;

    public static Writer nullWriter() { // ...}

        public void write(int c) throws IOException { // ...}
            public void write(char cbuf[]) throws IOException { // ...}
                public void write(String str) throws IOException { // ...} 
                    public void write(String str, int off, int len) throws IOException { // ...}

                        public Writer append(CharSequence csq) throws IOException { // ...}
                            public Writer append(CharSequence csq, int start, int end) throws IOException { // ...}
                                public Writer append(char c) throws IOException { // ...}         
                                }
```
__________________________________________________________________________________________________________________________________________________________________________________

#### write(int)
``public void write(int c) throws IOException``
* **write** 1 character **c** vào **Writer**
* tham số **c** là code của ký tự, là 1 giá trị **integer** nằm trong khoảng **[0 ; 65535]**
* nếu giá trị code của **c** nằm ngoài phạm vi nói trên, nó sẽ bị bỏ qua

```java
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

public class Writer_Ex1 {
    private static final String path = "D:\\Learning\\Java\\JavaOOP\\src\\_45_Java_IO\\_04_CharacterIO_Streams\\outWriterFile.txt";

    public static void main(String[] args) throws IOException {
        File file = new File(path);
        file.getParentFile().mkdirs();

        Writer writer = new FileWriter(file, StandardCharsets.UTF_8);

        writer.write((int)'J'); // 74
        writer.write((int)'P'); // 80
        writer.write((int)'日'); // 26085
        writer.write(26412); // 本
        writer.write((int)'-'); // 45
        writer.write(20843); // 八
        writer.write((int)'洲'); // 27954

        writer.close();
    }
}
```
* OUTPUT

![img_6.png](img_6.png)

__________________________________________________________________________________________________________________________________________________________________________________

#### write(char[])
``public void write(char cbuf[]) throws IOException``
* **write** 1 character array **cbuf[]** vào **Writer**

```java
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

public class write_char_array {
    private static final String path = "D:\\Learning\\Java\\JavaOOP\\src\\_45_Java_IO\\_04_CharacterIO_Streams\\writeCharArray.txt";

    public static void main(String[] args) throws IOException {
        File file = new File(path);
        file.getParentFile().mkdirs();

        Writer writer = new FileWriter(file, StandardCharsets.UTF_8);

        writer.write((int) 'J');

        char[] cbuf = new char[] {'P', '日', '本', '-', '八', '洲'};

        writer.write(cbuf);

        writer.close();
    }
}
```
* OUTPUT

![img_7.png](img_7.png)

__________________________________________________________________________________________________________________________________________________________________________________

#### write(char[], int, int)
``public void write(char[] cbuf, int off, int len) throws IOException``
* **write** 1 phần của character array vào **Writer**
* **write** các character từ vị trí **off** đến **off+len**
```java
import java.io.StringWriter;

public class write_char_arr_off_len {
    public static void main(String[] args) {
        StringWriter stringWriter = new StringWriter();

        char[] cbuf = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

        stringWriter.write(cbuf, 2, 5); // '2'...'6'

        String s = stringWriter.toString();

        System.out.println(s);
    }
}
```
* OUTPUT
```
23456
```
__________________________________________________________________________________________________________________________________________________________________________________
#### close()
``public void close() throws IOException``
* method **close()** được sử dụng để **đóng stream**
* method **flush()** sẽ được gọi đầu tiên
* khi stream đã bị đóng, các lệnh gọi thêm **write()** hoặc **flush()** sẽ khiến **IOException** được **throws** ra
* đóng stream đã đóng trước đó không có tác dụng
* **abstract class Writer implements Closeable**, **interface Closeable extends AutoCloseable**, vì vậy nếu ta viết code theo quy tắc của **AutoCloseable** thì hệ thống sẽ tự đóng stream mà không cần gọi method **close()**

```java
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

public class close_automation {
    private static final String path = "D:\\Learning\\Java\\JavaOOP\\src\\_45_Java_IO\\_04_CharacterIO_Streams\\close_automation.txt";

    public static void main(String[] args) throws IOException {
        File file = new File(path);
        file.getParentFile().mkdirs();

        try (Writer writer = new FileWriter(file, StandardCharsets.UTF_8)) {
            writer.write((int) 'J'); // 74
            writer.write((int) 'P'); // 80
            writer.write((int) '日'); // 26085
            writer.write(26412); // 本
            writer.write((int) '-'); // 45
            writer.write(20843); // 八
            writer.write((int) '洲'); // 27954
        }
    }
}
```
* OUTPUT

![img_8.png](img_8.png)

__________________________________________________________________________________________________________________________________________________________________________________
#### write(String)
``public void write(String str) throws IOException``
* **write** 1 **String** vào **Writer**
* ví dụ:
```java
writer.write("Some String");
```
__________________________________________________________________________________________________________________________________________________________________________________
#### write(String, int, int)
``public void write(String str, int offset, int len) throws IOException``
* **write** 1 phần của 1 **String** vào **Writer**
* **write** các character từ chỉ số **offset** đến chỉ số **offset + len** vào **Writer**
* ví dụ:
```java
writer.write("0123456789", 2, 5); // 23456
```
__________________________________________________________________________________________________________________________________________________________________________________
#### append(CharSequence)
``public Writer append(CharSequence csq) throws IOException``
* nối chuỗi ký tự của **CharSequence** vào **Writer**
* method này trả về object **Writer** hiện tại, điều này cho phép gọi tiếp 1 method khác của **Writer** thay vì kết thúc bởi dấu chấm phẩy ``;``
```java
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class append_CharSequence {
    public static void main(String[] args) throws IOException {
        Writer writer = new StringWriter();

        writer.append("01234").append("5678").write("9");

        System.out.println(writer.toString());

        writer.close();
    }
}
```
* OUTPUT
```
0123456789
```
__________________________________________________________________________________________________________________________________________________________________________________
#### append(CharSequence, int, int)
``public Writer append(CharSequence csq, int start, int end) throws IOException``
* nối 1 phần chuỗi ký tự của **CharSequence** vào **Writer**
* vị trí bắt đầu lấy ký tự để nối là ``start``
* vị trí kết thúc lấy ký tự để nối là ``end``
* method này trả về object **Writer** hiện tại, điều này cho phép gọi tiếp 1 method khác của **Writer** thay vì kết thúc bởi dấu chấm phẩy ``;``
```java
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class append_CharSequence_int_int {
    public static void main(String[] args) throws IOException {
        Writer writer = new StringWriter();

        String s = "0123456789";

        writer.append(s, 0, 3)
                .append(s, 3, 7)
                .append(s, 7, 9)
                .write("9");

        System.out.println(writer.toString());

        writer.close();
    }
}
```
* OUTPUT
```
0123456789
```
__________________________________________________________________________________________________________________________________________________________________________________
#### append(char)
``public Writer append(char chr) throws IOException``
* nối 1 ký tự vào **Writer**
* method này trả về object **Writer** hiện tại, điều này cho phép gọi tiếp 1 method khác của **Writer** thay vì kết thúc bởi dấu chấm phẩy ``;``
```java
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class append_char {
    public static void main(String[] args) throws IOException {
        Writer writer = new StringWriter();

        writer.append('J')
                .append('P')
                .append('日')
                .append('本');

        System.out.println(writer.toString());

        writer.close();
    }
}
```
* OUTPUT
```
JP日本
```
__________________________________________________________________________________________________________________________________________________________________________________
#### flush()
``public void flush() throws IOException``
* dữ liệu được ghi vào **Writer**, đôi khi được lưu trữ 1 cách tạm thời trên bộ đệm, method **flush()** được sử dụng để xả (flush) toàn bộ dữ liệu trên bộ đệm vào mục tiêu
* nếu mục tiêu dự kiến của **Writer** này là 1 phần trừu tượng được cung cấp bên dưới hệ điều hành, chẳng hạn như 1 tệp, <br/>
  thì việc **xả stream** chỉ đảm bảo rằng các ký tự trước đó đã được ghi vào stream sẽ được chuyển đến hệ điều hành để ghi, nó không đảm bảo rằng chúng thực sự được ghi<br/>
  vào 1 thiết bị vật lý như ổ đĩa
__________________________________________________________________________________________________________________________________________________________________________________

## 2. Reader <a id="2"></a>
* **abstract class Reader** nằm trong package **java.io**
* **Reader** là cơ sỡ đại diện cho các **Character Input Stream**
* **Reader** dùng để đọc 1 nguồn dữ liệu nào đó

![img_9.png](img_9.png)

* **Reader** được định nghĩa như sau
```java
public abstract class Reader extends Object implements Readable, Closeable {
    // ...    
}
```
* ta không thể sử dụng **Reader** 1 cách trực tiếp, vì nó là một **abstract class**, tùy theo tình huống cụ thể, ta có thể sử dụng các **subclass** của nó
* các **subclass** của **Reader**
    * ``BufferedReader``
    * ``LineNumberReader``
    * ``CharArrayReader``
    * ``FilterReader``
    * ``PushbackReader``
    * ``InputStreamReader``
    * ``FileReader``
    * ``PipedReader``
    * ``StringReader``

* xét ví dụ đọc 1 file văn bản với **encoding UTF-8**

![img_3.png](img_3.png)

* **UTF-8** sử dụng 1, 2, 3 hoặc 4 bytes để lưu trữ 1 ký tự, hình ảnh dưới đây cho thấy các byte trong file nói trên

![img_10.png](img_10.png)

* **FileReader** là 1 **subclass** của **Reader** thường được sử dụng để đọc các file văn bản, và chúng ta sẽ nhận được 1 **stream of characters**
* bằng 1 cách nào đó các ký tự **UTF-8** sẽ được chuyển đổi thành các ký tự của **Java**
* kiểu **char** trong **Java** có kích thước 2 bytes, như vậy các ký tự trên **FileReader** có kích thước là 2 bytes

![img_11.png](img_11.png)

__________________________________________________________________________________________________________________________________________________________________________________

### Reader Fields
```java
public abstract class Reader extends Object implements Readable, Closeable {
    private static final int TRANSFER_BUFFER_SIZE = 8192;
    private static final int maxSkipBufferSize = 8192;
    private char skipBuffer[] = null;
    protected Object lock;
    // ...    
}
```
* ``lock`` là object được sử dụng để **synchronize** các hoạt động trên stream này
__________________________________________________________________________________________________________________________________________________________________________________
### Reader Constructor
```java
public abstract class Reader extends Object implements Readable, Closeable {
    protected Reader() {// ...}
    protected Reader(Object lock) {// ...}
        }
```
* ``protected Reader()``
    * tạo mới 1 character stream reader với các phần quan trọng sẽ được synchronize trên chính Reader

* ``protected Reader(Object lock)``
    * tạo mới 1 character stream reader với các phần quan trọng sẽ được synchronize trên chính object chỉ định
__________________________________________________________________________________________________________________________________________________________________________________
### Reader Methods
```java
public abstract class Reader extends Object implements Readable, Closeable {
    public static Reader nullReader() {//...}

        public abstract int read(char cbuf[], int off, int len) throws IOException;
        public abstract void close() throws IOException;

        public int read(java.nio.CharBuffer target) throws IOException {//...}
            public int read() throws IOException {//...}
                public int read(char cbuf[]) throws IOException {//...}

                    public boolean ready() throws IOException {//...}
                        public boolean markSupported() {//...}
                            public void mark(int readAheadLimit) throws IOException {//...}
                                public void reset() throws IOException {//...}
                                    public long skip(long n) throws IOException {//...}
                                        public long transferTo(Writer out) throws IOException {//...}
                                        }
```
__________________________________________________________________________________________________________________________________________________________________________________

#### read()
* ``public int read() throws IOException``
    * method **read()** được sử dụng để **read** 1 character
    * nó trả về mã code của character vừa đọc được, là 1 giá trị integer trong khoảng **[0 ; 65535]**
    * hoặc trả về **-1** nếu đọc tới cuối stream
    * method này sẽ bị block cho đến khi có character ready để read, hoặc xảy ra lỗi **IO**, hoặc đã tiến tới cuối stream

* ví dụ về method **read()**
```java
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class read {
    public static void main(String[] args) throws IOException {
        Reader reader = new StringReader("JP日本-八洲");

        int charCode = 0;

        while ((charCode = reader.read()) != -1) {
            System.out.println((char) charCode + "\t" + charCode);
        }

        reader.close();
    }
}
```
* OUTPUT
```
J	74
P	80
日	26085
本	26412
-	45
八	20843
洲	27954
```
__________________________________________________________________________________________________________________________________________________________________________________
#### read(char[])
* ``public int read(char[] cbuf) throws IOException``
    * method **read(char[])** đọc các ký tự và gán các ký tự vào mảng **char[]**, số lượng ký tự mỗi lần đọc đúng bằng kích thước của mảng
    * method này trả về số lượng ký tự đọc được sau mỗi lần đọc từ stream
    * method này trả về **-1** nếu đọc tới cuối stream
    * method này sẽ bị block cho đến khi có các ký tự **ready** để đọc, hoặc xảy ra lỗi **IO**, hoặc đã tiến tới cuối stream
    * về cơ bản sử dụng method **read(char[])** sẽ có hiệu suất cao hơn so với method **read()**, vì nó giảm số lần đọc từ stream

* ví dụ về method **read(char[])**
```java
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class read_to_char_array {
    public static void main(String[] args) throws IOException {
        Reader reader = new StringReader("1234567891234567891100");

        char[] cbuf = new char[10];
        int charCount = -1;

        while ((charCount = reader.read(cbuf)) != -1) {
            String s = new String(cbuf, 0, charCount);
            System.out.println(charCount + "\t" + s);
        }

        reader.close();
    }
}
```
* OUTPUT
```
10	1234567891
10	2345678911
2	00
```
__________________________________________________________________________________________________________________________________________________________________________________
#### read(char[], int, int)
* ``public int read(char[] cbuf, int offset, int len) throws IOException``
    * method **read(char[], int, int)** đọc các ký tự, và gán các ký tự vào mảng **char[]** từ chỉ số **offset** đến chỉ số **offset + len**
    * method này trả về số lượng ký tự mỗi lần đọc được từ stream
    * method này trả về **-1** nếu đã đọc tới cuối stream
    * method này sẽ bị block cho đến khi có các ký tự sẵn sàng để đọc, xảy ra lỗi **IO**, hoặc đã tiến tới cuối stream

__________________________________________________________________________________________________________________________________________________________________________________
#### read(java.nio.CharBuffer)
* ``public int read(java.nio.CharBuffer target) throws IOException``
    * method **read(CharBuffer)** đọc các ký tự, và truyền các ký tự vào object chỉ định của **CharBuffer** là ``target``
    * method này trả về số ký tự đọc được từ stream
    * hoặc trả về **-1** nếu tiến tới cuối stream
    * method này sẽ bị block cho đến khi có các ký tự sẵn sàng để đọc, xảy ra lỗi **IO**, hoặc đã tiến tới cuối stream**

* ví dụ về method **read(CharBuffer)**
```java
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.CharBuffer;

public class read_CharBuffer {
    public static void main(String[] args) throws IOException {
        Reader reader = new StringReader("0123456789012456789");

        CharBuffer charBuffer = CharBuffer.allocate(7);

        int charCount;

        while ((charCount = reader.read(charBuffer)) != -1) {
            System.out.println(charCount + "\t" + charBuffer.flip().toString());
        }

        reader.close();
    }
}
```
* OUTPUT
```
7	0123456
7	7890124
5	56789
```
__________________________________________________________________________________________________________________________________________________________________________________
#### close()
* ``public void close() throws IOException ``
    * đóng stream và giải phóng mọi **system resource** đang liên kết đến stream
    * khi stream bị đóng, các lệnh gọi tiếp **read(), ready(), mark(), reset(), skip()** sẽ tạo ra 1 **IOException**
    * đóng stream đã đóng trước đó không có tác dụng
    * **abstract class Reader implements Closeable**, **interface Closeable extends AutoCloseable**, nếu lập trình theo quy tắc của **AutoCloseable** thì hệ thống sẽ tự đóng stream mà không cần gọi thủ công method **close()**

```java
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class close_AutoCloseable {
    public static void main(String[] args) throws IOException {
        try (Reader reader = new StringReader("01234567890123456789")){
            int charCount;
            char[] cbuf = new char[5];
            while ((charCount = reader.read(cbuf)) != -1) {
                String s = new String(cbuf, 0, charCount);
                System.out.println(charCount +"\t" + s);
            }
        }
    }
}
```
* OUTPUT
```
5	01234
5	56789
5	01234
5	56789
```
__________________________________________________________________________________________________________________________________________________________________________________
#### skip(long)
* ``public long skip(long n) throws IOException``
    * method **skip(long)** bỏ qua không đọc ``n`` ký tự trong stream
    * method này sẽ bị block cho đến khi có các ký tự sẵn có, hoặc xảy ra lỗi **IO**, hoặc đã tiến tới cuối stream

* ví dụ về method **skip(long)**
```java
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class skip {
    public static void main(String[] args) throws IOException {
        Reader reader = new StringReader("0123456789ABCDEF");

        System.out.println((char) reader.read());
        System.out.println((char) reader.read());
        reader.skip(8);

        int charCode;
        while ((charCode = reader.read()) != -1) {
            System.out.println((char) charCode);
        }

        reader.close();
    }
}
```
* OUTPUT
```
0
1
A
B
C
D
E
F
```
__________________________________________________________________________________________________________________________________________________________________________________
#### transferTo(Writer)
* ``public long transferTo(Writer out) throws IOException``
    * method **transferTo(Writer)** được sử dụng để đọc tất cả các ký tự từ **Reader** hiện tại, và ghi chúng vào đối tượng **Writer** chỉ định
    * method này trả về số lượng ký tự đã chuyển sang **Writer**, sau khi kết thúc, đối tượng **Reader** hiện tại sẽ ở cuối stream
    * method này sẽ **không đóng** đối tượng **Reader** hiện tại cũng như đối tượng **Writer**
* ví dụ về method **transferTo(Writer)
```java
import java.io.*;

public class transferTo_Writer {
    public static void main(String[] args) throws IOException {
        Reader reader = new StringReader("123456789-987654321-ABCDE");

        File file = new File("D:\\Learning\\Java\\JavaOOP\\src\\_45_Java_IO\\_04_CharacterIO_Streams\\transferToWriter.txt");
        file.getParentFile().mkdirs();
        Writer writer = new FileWriter(file);

        reader.skip(10);

        reader.transferTo(writer);

        writer.close();
        reader.close();
    }
}
```

* OUTPUT

![img_12.png](img_12.png)


__________________________________________________________________________________________________________________________________________________________________________________
#### markSupported()
* ``public boolean markSupported()``
    * method **markSupported()** được sử dụng để kiểm tra đối tượng **Reader** có hỗ trợ hoạt động **mark(int)** hay không

Reader              | markSupported() ?
:-------------------|:-----------------
StringReader        |true
CharArrayReader     |true
BufferedReader      |true
LineNumberReader    |true
FilterReader        |true or false
InputStreamReader   |false
FileReader          |false
PushbackReader      |false

__________________________________________________________________________________________________________________________________________________________________________________
#### mark(int)
* ``public void mark(int readAheadLimit) throws IOException``
    * method **mark(int)** cho phép đánh dấu vị trí hiện tại trên stream
    * sau khi **mark(int)** có thể đọc tiếp các ký tự tiếp theo, và gọi method **reset()** để quay trở lại vị trí đã **mark(int)** trước đó
    * ``readAheadLimit`` là số lượng ký tự tối đa có thể đọc sau khi **mark(int)** mà không làm mất vị trí đã **mark(int)**
    * không phải tất cả các **subclass** của **Reader** có hỗ trợ **mark(int)**, <br/>
      để chắc chắn, trước khi thực hiện thao tác **mark(int)** cần gọi method **markSupported()** để kiểm tra xem đối tượng **Reader** hiện tại có hỗ trợ hoạt động **mark(int)** hay không

* ví dụ về method **mark(int)**
```java
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class mark_int {
    public static void main(String[] args) throws IOException {
        Reader reader = new StringReader("----------ABCDEF123456");

        reader.skip(10);

        System.out.println("markSupported() ? - " + reader.markSupported());
        reader.mark(6);
        System.out.println("mark(int)");

        int count = 0;

        while (!(count == 6)) {
            char c = (char) reader.read();
            System.out.println(c);
            count++;
        }

        reader.reset();
        System.out.println("reset()");

        int charCode = 0;
        while ((charCode = reader.read()) != -1) {
            System.out.println((char) charCode);
        }

        reader.close();
    }
}
```
* OUTPUT
```
markSupported() ? - true
mark(int)
A
B
C
D
E
F
reset()
A
B
C
D
E
F
1
2
3
4
5
6
```

__________________________________________________________________________________________________________________________________________________________________________________
#### reset()
* ``public void reset() throws IOException``
    * nếu đối tượng **Reader** hiện tại hỗ trợ việc đánh dấu vị trí hiện tại thông qua method **mark(int)**, sau khi đã kiểm tra trả về ``true`` với method **markSupported()**
    * thì method **reset()** được sử dụng để quay trở lại vị trí đã được đánh dấu, với điều kiện vị trí đang đọc không vượt quá giới hạn trong method **mark(int)**

__________________________________________________________________________________________________________________________________________________________________________________
#### ready()
* ``public boolean ready() throws IOException``
    * method **ready()** trả về ``true`` nếu sẵn có 1 ký tự trên stream để đọc mà không phải chờ đợi, ngược lại trả về ``false``
__________________________________________________________________________________________________________________________________________________________________________________

## 3. OutputStreamWriter <a id="3"></a>
* **OutputStreamWriter** được đóng gói trong package **java.io**
* **OutputStreamWriter** là 1 **subclass** của **abstract class Writer**, nó là 1 cầu nối cho phép chuyển 1 **character stream** thành 1 **byte stream**, hay nói cách khác là 1 **Writer** thành 1 **OutputStream**
* **OutputStreamWriter** hoạt động với các **output stream** khác, là cầu nối giữa **character stream** và **byte stream**, điều này là do **OutputStreamWriter** chuyển đối các ký tự thành dạng byte
* ví dụ, 1 số ký tự yêu cầu 2 byte được lưu trữ trong bộ nhớ, để ghi dữ liệu như vậy, chúng ta có thể sử dụng **OutputStreamWriter** để chuyển đổi ký tự thành các byte tương ứng và lưu trữ các byte lại với nhau


### OutputStreamWriter Constructors
```java
public class OutputStreamWriter extends Writer {

    public OutputStreamWriter(OutputStream out) {//...}
    public OutputStreamWriter(OutputStream out, String charsetName) throws UnsupportedEncodingException {//...}
    public OutputStreamWriter(OutputStream out, Charset cs) {//...}
    public OutputStreamWriter(OutputStream out, CharsetEncoder enc) {//...}
}
```
Constructor                                                 |Description
:-----------------------------------------------------------|:--------------------------------------------------------------
OutputStreamWriter(OutputStream out)                        |tạo 1 đối tượng OutputStreamWriter sử dụng character encoding mặc định, <br/>tham số là 1 đối tượng OutputStream
OutputStreamWriter(OutputStream out, String charsetName)    |tạo 1 đối tương OutputStreamWriter sử dụng charset được đặt tên, <br/>tham số 1 là OutputStream, tham số 2 là chuỗi tên của charset được hỗ trợ, <br/>UnsupportedEncodingException được ném ra nếu encoding được đặt tên không hỗ trợ
OutputStreamWriter(OutputStream out, Charset cs)            |tạo 1 đối tượng OutputStreamWriter sử dụng charset được chỉ định, <br/>tham số 1 là OutputStream, tham số 2 là CharSet chỉ định
OutputStreamWriter(OutputStream out, CharsetEncoder enc)    |tạo 1 đối tượng OutputStreamWriter sử dụng charset encoder chỉ định, <br/>tham số 1 là OutputStream, tham số 2 là CharsetEncoder

__________________________________________________________________________________________________________________________________________________________________________________

### OutputStreamWriter Methods
```java
public class OutputStreamWriter extends Writer {

    public String getEncoding() {//...}
    void flushBuffer() throws IOException {//...}
    public void write(int c) throws IOException {//...}
    public void write(char cbuf[], int off, int len) throws IOException {//...}
    public void write(String str, int off, int len) throws IOException {//...}

    @Override
    public Writer append(CharSequence csq, int start, int end) throws IOException {//...}
    
    @Override
    public Writer append(CharSequence csq) throws IOException {//...}
    public void flush() throws IOException {//...}
    public void close() throws IOException {//...}
}
```
* những method của riêng **OutputStreamWriter** 

Return Data |Methods                                |Description
:-----------|:--------------------------------------|:-------------------
String      |getEncoding()                          |trả về tên của character encoding đang được sử dụng bởi stream này, <br/>trả về ``null`` nếu stream đã bị đóng
void        |write(int c)                           |ghi 1 ký tự vào stream, <br/>tham số là mã code của ký tự có kiểu integer
void        |write(char[] cbuf, int off, int len)   |ghi 1 phần của mảng ký tự vào stream, <br/>``cbuf`` : mảng ký tự cần ghi, <br/>``off`` : vị trí bắt đầu lấy dữ liệu trong mảng, <br/>``len`` : số lượng ký tự trong mảng cần lấy bắt đầu từ ``off``
void        |write(String str, int off, int len)    |ghi 1 phần của chuỗi vào stream, <br/>``str`` : chuỗi ký tự cần ghi, <br/>``off`` : vị trí bắt đầu lấy dữ liệu trong chuỗi để ghi, <br/>``len`` : số lượng ký tự trong chuỗi cần lấy bắt đầu từ ``off``
void        |flush()                                |đẩy toàn bộ dữ liệu trên stream xuống mục tiêu cần lưu trữ

__________________________________________________________________________________________________________________________________________________________________________________

### OutputStreamWriter với UTF-16
* **UTF-16** là mã hóa (encoding) khá phổ biến đối với văn bản tiếng Trung Quốc hoặc tiếng Nhật
* ví dụ ghi 1 file với **encoding UTF-16**
    * nội dung cần ghi vào file: ``JP日本-八洲``
    * chúng ta sử dụng **UTF-16 OutputStreamWriter** để ghi các ký tự vào 1 file
    * sau đó sử dụng **FileInputStream** để đọc từng byte dữ liệu của file đã được ghi
    
```java
import java.io.*;
import java.nio.charset.StandardCharsets;

public class OutputStreamWriter_UTF16_Ex1 {
    private static String path = "D:\\Learning\\Java\\JavaOOP\\src\\_45_Java_IO\\_04_CharacterIO_Streams\\_03_OutputStreamWriter\\utf16_file_out.txt";

    public static void main(String[] args) throws IOException {
        System.out.println("--- write UTF-16 file ---");
        write_UTF16_Character_stream();

        System.out.println("--- read file as a binary stream ---");
        read_as_a_Binary_stream();
    }

    private static void write_UTF16_Character_stream() throws IOException {
        File file = new File(path);
        file.getParentFile().mkdirs();

        OutputStream os = new FileOutputStream(file);
        Writer osw = new OutputStreamWriter(os, StandardCharsets.UTF_16);

        String s = "JP日本-八洲";

        osw.write(s);

        osw.close();
        os.close();
    }

    private static void read_as_a_Binary_stream() throws IOException {
        InputStream is = new FileInputStream(path);

        int byteCode;
        while ((byteCode = is.read()) != -1) {
            System.out.println((char) byteCode + "\t" + byteCode);
        }

        is.close();
    }
}
```
* OUTPUT

![img_13.png](img_13.png)

```
--- write UTF-16 file ---
--- read file as a binary stream ---
þ	254
ÿ	255
 	0
J	74
 	0
P	80
e	101
å	229
g	103
,	44
 	0
-	45
Q	81
k	107
m	109
2	50
```
* trong **Java**, kiểu dữ liệu **char** có kích thước 2 **byte**, và **UTF-16** được sử dụng để mã hóa kiểu **String**
* hình ảnh dưới đây cho thấy các ký tự trên **OutputStreamWriter**

![img_14.png](img_14.png)

* phân tích các **byte** trên file vừa được tạo ra cho thấy:
    * 2 byteCode đầu tiên là: **(254, 255)** được sử dụng để đánh dấu rằng đây là 1 văn bản được mã hóa **UTF-16**, chúng còn được gọi là **BOM (Byte Order Mark)**
    * các **byte** tiếp theo giống các **byte** trên **OutputStreamWriter**
  
![img_15.png](img_15.png)

__________________________________________________________________________________________________________________________________________________________________________________
### OutputStreamWriter với UTF-8
* **UTF-8** là mã hóa (encoding) phổ biến nhất thế giới, nó có thể mã hóa mọi chữ viết trên thế giới bao gồm cả ký tự của Trung Quốc, Nhật Bản
* bắt đầu từ **Java5, UTF-8** là mã hóa mặc định khi đọc ghi file
* các file **UTF-8** được tạo ra bởi **Java** sẽ không có **BOM (Byte Order Mark)** - các bytes đầu tiên của file để đánh dấu rằng đây là 1 file **UTF-16**
* ví dụ ghi 1 file với **encoding UTF-8**
  * nội dung cần ghi vào file: ``JP日本-八洲``
  * chúng ta sử dụng **UTF-8 OutputStreamWriter** để ghi các ký tự vào 1 file
  * sau đó sử dụng **FileInputStream** để đọc từng byte dữ liệu của file đã được ghi
  
```java
import java.io.*;
import java.nio.charset.StandardCharsets;

public class OutputStreamWriter_UTF8 {
    private static String path = "D:\\Learning\\Java\\JavaOOP\\src\\_45_Java_IO\\_04_CharacterIO_Streams\\_03_OutputStreamWriter\\utf8_file_out.txt";

    public static void main(String[] args) throws IOException {
        System.out.println("--- write UTF-8 file ---");
        write_UTF8_Character_stream();

        System.out.println("--- read as a Binary stream ---");
        read_as_a_Binary_stream();
    }

    private static void write_UTF8_Character_stream() throws IOException {
        File file = new File(path);
        file.getParentFile().mkdirs();

        OutputStream os = new FileOutputStream(file);
        Writer osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);

        String s = "JP日本-八洲";

        osw.write(s);

        osw.close();
        os.close();
    }

    private static void read_as_a_Binary_stream() throws IOException {
        InputStream is = new FileInputStream(path);

        int byteCode;
        while ((byteCode = is.read()) != -1) {
            System.out.println((char) byteCode + "\t" + byteCode);
        }

        is.close();
    }
}
```
* OUTPUT
```
--- write UTF-8 file ---
--- read as a Binary stream ---
J	74
P	80
æ	230
	151
¥	165
æ	230
	156
¬	172
-	45
å	229
	133
«	171
æ	230
´	180
²	178
```
* trong **Java**, kiểu dữ liệu **char** có kích thước 2 **byte**, **UTF-16** được sử dụng để mã hóa kiểu **String**
* hình ảnh dưới đây cho thấy các ký tự trên **OutputStreamWriter**

![img_16.png](img_16.png)

* **UTF-8** được mã hóa phức tạp hơn khá nhiều so với **UTF-16**, nó sử dụng **1, 2, 3** hoặc **4 bytes** để lưu trữ 1 ký tự
* phân tích các byte trên file **UTF-8** được tạo ra cho thấy điều đó

![img_17.png](img_17.png)

* ví dụ ký tự **"日"** có mã số là **26085** thuộc phạm vi **[2048 ; 65535]**, **UTF-8** sẽ cần 3 **bytes** để lưu trữ nó

![img_18.png](img_18.png)

__________________________________________________________________________________________________________________________________________________________________________________

## 4. InputStreamReader <a id="4"></a>
* **InputStreamReader** được đóng gói trong package **java.io**
* **InputStreamReader** là cầu nối từ 1 **byte stream** sang 1 **character stream**, nó đọc các bytes và decode chúng thành các characters bằng cách sử dụng 1 **charset** chỉ định
* **charset** mà **InputStreamReader** sử dụng có thể được chỉ định theo tên, hoặc được chỉ định 1 cách tường minh, hoặc là mặc định theo platform

### InputStreamReader Constructors
```java
public class InputStreamReader extends Reader {
  public InputStreamReader(InputStream in) {//...}
  public InputStreamReader(InputStream in, String charsetName) throws UnsupportedEncodingException {//...}
  public InputStreamReader(InputStream in, Charset cs) {//...}
  public InputStreamReader(InputStream in, CharsetDecoder dec) {//...}
}
```

Constructor                                                 |Description
:-----------------------------------------------------------|:--------------------------------------------------------------
InputStreamReader(InputStream in)                           |tạo 1 đối tượng **InputStreamReader** sử dụng bộ **charset** mặc định, <br/>``in`` : tham số truyền vào là **InputStream**
InputStreamReader(InputStream in, String charsetName)       |tạo 1 đối tượng **InputStreamReader** sử dụng bộ **charset** chỉ định theo tên được đặt,<br/>``in`` : tham số 1 là đối tượng **InputStream**,<br/>``charsetName`` : tham số 2 là tên của bộ **charset** đã được đặt tên
InputStreamReader(InputStream in, Charset cs)               |tạo 1 đối tượng **InputStreamReader** sử dụng bộ **charset** chỉ định tường minh,<br/>``in`` : tham số 1 là đối tượng **InputStream**,<br/>``cs`` : tham số 2 là bộ **charset** chỉ định tường minh
InputStreamReader(InputStream in, CharsetDecoder dec)       |tạo 1 đối tượng **InputStreamReader** sử dụng bộ **charset decoder** chỉ định,<br/>``in`` : tham số 1 là đối tượng **InputStream**,<br/>``dec`` : tham số 2 là bộ **charset decoder** chỉ định

### InputStreamReader Methods
```java
public class InputStreamReader extends Reader {
  public String getEncoding() {//...}
  public int read() throws IOException {//...}
  public int read(char cbuf[], int offset, int length) throws IOException {//...}
  public boolean ready() throws IOException {//...}
  public void close() throws IOException {//...}
}
```

Return Data |Method                                     |Description
:-----------|:------------------------------------------|:-------------------------------------------------------
String      |getEncoding()                              |trả về tên của **character encoding** đang được sử dụng bởi stream này
int         |read()                                     |trả về **byteCode** của ký tự đọc được trong stream<br/>trả về **-1** nếu tiến tới cuối stream
int         |read(char cbuf[], int offset, int length)  |đọc các ký tự trong stream, gán các ký tự vào mảng **char** ``cbuf`` từ vị trí ``offset`` đến vị trí ``offset + length``<br/>trả về số lượng ký tự đọc được trong stream<br/>trả về **-1** nếu tiến tới cuối stream
boolean     |ready()                                    |trả về ``true`` nếu có sẵn ký tự kế tiếp có thể đọc mà không bị **block**
void        |close()                                    |đóng stream hiện tại

__________________________________________________________________________________________________________________________________________________________________________________

### InputStreamReader với UTF16
* ví dụ về sử dụng **InputStreamReader** để đọc 1 file **UTF16**
```java
import java.io.*;
import java.nio.charset.StandardCharsets;

public class InputStreamReader_UTF16 {
    private static String path = "D:\\Learning\\Java\\JavaOOP\\src\\_45_Java_IO\\_04_CharacterIO_Streams\\_03_OutputStreamWriter\\utf16_file_out.txt";

    public static void main(String[] args) throws IOException {
        System.out.println("--- read character in character stream using InputStreamReader with utf16 file ---");
        readAs_UTF16_CharacterStream();
        System.out.println();

        System.out.println("--- read as a binary stream ---");
        readAs_BinaryStream();
    }

    private static void readAs_UTF16_CharacterStream() throws IOException {
        File file = new File(path);
        InputStream is = new FileInputStream(file);
        Reader isr = new InputStreamReader(is, StandardCharsets.UTF_16);

        int charCode;
        while ((charCode = isr.read()) != -1) {
            System.out.println((char) charCode + "\t" + charCode);
        }

        isr.close();
        is.close();
    }

    private static void readAs_BinaryStream() throws IOException {
        File file = new File(path);
        InputStream is = new FileInputStream(file);

        int charCode;
        while ((charCode = is.read()) != -1) {
            System.out.println((char) charCode + "\t" + charCode);
        }

        is.close();
    }
}
```
* OUTPUT
```
--- read character in character stream using InputStreamReader with utf16 file ---
J	74
P	80
日	26085
本	26412
-	45
八	20843
洲	27954

--- read as a binary stream ---
þ	254
ÿ	255
 	0
J	74
 	0
P	80
e	101
å	229
g	103
,	44
 	0
-	45
Q	81
k	107
m	109
2	50
```
* hình ảnh dưới đây cho thấy các **bytes** trong file **UTF16**, 2 **bytes** đầu tiên **(254, 255)** được sử dụng để đánh dấu nó bắt đầu của 1 văn bản **UTF16**

![img_19.png](img_19.png)

* **UTF16 InputStreamReader** đọc 2 **bytes** đầu tiên để xác định kiểu mã hóa của văn bản, và biết rằng nó đang làm việc với 1 văn bản **UTF16**, nó tiến hành ghép 2 byte liên tiếp thành 1 ký tự
__________________________________________________________________________________________________________________________________________________________________________________
### InputStreamReader với UTF8
* **UTF-8** là mã hóa (encoding) phổ biến nhất trên thế giới, nó có thể mã hóa mọi chữ viết trên thế giới bao gồm cả ký tự Trung Quốc, Nhật Bản
* bây giờ chúng ta hãy phân tích làm thế nào **InputStreamReader** đọc được các văn bản **UTF-8**

```java
import java.io.*;
import java.nio.charset.StandardCharsets;

public class InputStreamReader_UTF8 {
    private static final String path = "D:\\Learning\\Java\\JavaOOP\\src\\_45_Java_IO\\_04_CharacterIO_Streams\\_03_OutputStreamWriter\\utf8_file_out.txt";

    public static void main(String[] args) throws IOException {
        System.out.println("--- readAs_UTF8_CharacterStream ---");
        readAs_UTF8_CharacterStream();
        System.out.println();

        System.out.println("--- readAs_BinaryStream ---");
        readAs_BinaryStream();
    }

    private static void readAs_UTF8_CharacterStream() throws IOException {
        InputStream is = new FileInputStream(path);
        Reader isr = new InputStreamReader(is, StandardCharsets.UTF_8);

        int charCode;
        while ((charCode = isr.read()) != -1) {
            System.out.println((char) charCode + "\t" + charCode);
        }

        isr.close();
        is.close();
    }

    private static void readAs_BinaryStream() throws IOException {
        InputStream is = new FileInputStream(path);

        int charCode;
        while ((charCode = is.read()) != -1) {
            System.out.println((char) charCode + "\t" + charCode);
        }

        is.close();
    }
}
```
* OUTPUT
```
--- readAs_UTF8_CharacterStream ---
J	74
P	80
日	26085
本	26412
-	45
八	20843
洲	27954

--- readAs_BinaryStream ---
J	74
P	80
æ	230
	151
¥	165
æ	230
	156
¬	172
-	45
å	229
	133
«	171
æ	230
´	180
²	178
```
* hình ảnh dưới đây cho thấy các **bytes** trong file **UTF-8**

![img_20.png](img_20.png)

* mã hóa ký tự **UTF-8** phức tạp hơn rất nhiều so với **UTF-16**, nó cần **1, 2, 3,** hoặc **4 bytes** để lưu trữ 1 ký tự, điều này phụ thuộc vào mã của ký tự

![img_21.png](img_21.png)

* hình ảnh dưới đây, là 1 ví dụ cho thấy cách **UTF-8 InputStreamReader** biến **3 bytes** của **UTF-8** thành 1 ký tự **2 bytes** của Java

![img_22.png](img_22.png)

__________________________________________________________________________________________________________________________________________________________________________________

__________________________________________________________________________________________________________________________________________________________________________________

## 5. FileWriter <a id="5"></a>
__________________________________________________________________________________________________________________________________________________________________________________

## 6. FileReader <a id="6"></a>
__________________________________________________________________________________________________________________________________________________________________________________

## 7. CharArrayWriter <a id="7"></a>
__________________________________________________________________________________________________________________________________________________________________________________

## 8. CharArrayReader <a id="8"></a>
__________________________________________________________________________________________________________________________________________________________________________________

## 9. FilterWriter <a id="9"></a>
__________________________________________________________________________________________________________________________________________________________________________________

## 10. FilterReader <a id="10"></a>
__________________________________________________________________________________________________________________________________________________________________________________

## 11. PushbackReader <a id="11"></a>
__________________________________________________________________________________________________________________________________________________________________________________

## 12. BufferedWriter <a id="12"></a>
__________________________________________________________________________________________________________________________________________________________________________________

## 13. BufferedReader <a id="13"></a>
__________________________________________________________________________________________________________________________________________________________________________________

## 14. LineNumberReader <a id="14"></a>
__________________________________________________________________________________________________________________________________________________________________________________

## 15. StringWriter <a id="15"></a>
__________________________________________________________________________________________________________________________________________________________________________________

## 16. StringReader <a id="16"></a>
__________________________________________________________________________________________________________________________________________________________________________________

## 17. PipedWriter <a id="17"></a>
__________________________________________________________________________________________________________________________________________________________________________________

## 18. PipedReader <a id="18"></a>
__________________________________________________________________________________________________________________________________________________________________________________

## 19. LineNumberReader <a id="19"></a>
__________________________________________________________________________________________________________________________________________________________________________________

## 20. PrintWriter <a id="20"></a>
__________________________________________________________________________________________________________________________________________________________________________________

## 21. PrintStream <a id="21"></a>
__________________________________________________________________________________________________________________________________________________________________________________
