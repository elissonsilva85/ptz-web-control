export class Faultylabs {

  p = null;
  q = null;

  constructor() { }

  b(a) {
      var b = (a >>> 0).toString(16);
      return "00000000".substr(0, 8 - b.length) + b
  }
  c(a) {
      for (var b = [], c = 0; c < a.length; c++)
          b = b.concat(this.k(a[c]));
      return b
  }
  d(a) {
      for (var b = [], c = 0; 8 > c; c++)
          b.push(255 & a),
          a >>>= 8;
      return b
  }
  e(a, b) {
      return a << b & 4294967295 | a >>> 32 - b
  }
  f(a, b, c) {
      return a & b | ~a & c
  }
  g(a, b, c) {
      return c & a | ~c & b
  }
  h(a, b, c) {
      return a ^ b ^ c
  }
  i(a, b, c) {
      return b ^ (a | ~c)
  }
  j(a, b) {
      return a[b + 3] << 24 | a[b + 2] << 16 | a[b + 1] << 8 | a[b]
  }
  k(a) {
      for (var b = [], c = 0; c < a.length; c++)
          if (a.charCodeAt(c) <= 127)
              b.push(a.charCodeAt(c));
          else
              for (var d = encodeURIComponent(a.charAt(c)).substr(1).split("%"), e = 0; e < d.length; e++)
                  b.push(parseInt(d[e], 16));
      return b
  }
  l(arg1, arg2, arg3, arg4) {
    let args = [arg1, arg2, arg3, arg4];
    for (var a = "", c = 0, d = 0, e = 3; e >= 0; e--)
        d = args[e],
        c = 255 & d,
        d >>>= 8,
        c <<= 8,
        c |= 255 & d,
        d >>>= 8,
        c <<= 8,
        c |= 255 & d,
        d >>>= 8,
        c <<= 8,
        c |= d,
        a += this.b(c);
    return a
  }
  m(a) {
      for (var b = new Array(a.length), c = 0; c < a.length; c++)
          b[c] = a[c];
      return b
  }
  n(a, b) {
      return 4294967295 & a + b
  }
  o() {
      let a = (a, b, c, d) => {
          var f = v;
          v = u,
          u = t,
          t = this.n(t, this.e(this.n(s, this.n(a, this.n(b, c))), d)),
          s = f
      }
      var b = this.p.length;
      this.p.push(128);
      var c = this.p.length % 64;
      if (c > 56) {
          for (var k = 0; 64 - c > k; k++)
          this.p.push(0);
          c = this.p.length % 64
      }
      for (k = 0; 56 - c > k; k++)
          this.p.push(0);
      this.p = this.p.concat(this.d(8 * b));
      var m = 1732584193
        , o = 4023233417
        , q = 2562383102
        , r = 271733878
        , s = 0
        , t = 0
        , u = 0
        , v = 0;
      for (k = 0; k < this.p.length / 64; k++) {
          s = m,
          t = o,
          u = q,
          v = r;
          var w = 64 * k;
          a(this.f(t, u, v), 3614090360, this.j(this.p, w), 7),
          a(this.f(t, u, v), 3905402710, this.j(this.p, w + 4), 12),
          a(this.f(t, u, v), 606105819, this.j(this.p, w + 8), 17),
          a(this.f(t, u, v), 3250441966, this.j(this.p, w + 12), 22),
          a(this.f(t, u, v), 4118548399, this.j(this.p, w + 16), 7),
          a(this.f(t, u, v), 1200080426, this.j(this.p, w + 20), 12),
          a(this.f(t, u, v), 2821735955, this.j(this.p, w + 24), 17),
          a(this.f(t, u, v), 4249261313, this.j(this.p, w + 28), 22),
          a(this.f(t, u, v), 1770035416, this.j(this.p, w + 32), 7),
          a(this.f(t, u, v), 2336552879, this.j(this.p, w + 36), 12),
          a(this.f(t, u, v), 4294925233, this.j(this.p, w + 40), 17),
          a(this.f(t, u, v), 2304563134, this.j(this.p, w + 44), 22),
          a(this.f(t, u, v), 1804603682, this.j(this.p, w + 48), 7),
          a(this.f(t, u, v), 4254626195, this.j(this.p, w + 52), 12),
          a(this.f(t, u, v), 2792965006, this.j(this.p, w + 56), 17),
          a(this.f(t, u, v), 1236535329, this.j(this.p, w + 60), 22),
          a(this.g(t, u, v), 4129170786, this.j(this.p, w + 4), 5),
          a(this.g(t, u, v), 3225465664, this.j(this.p, w + 24), 9),
          a(this.g(t, u, v), 643717713, this.j(this.p, w + 44), 14),
          a(this.g(t, u, v), 3921069994, this.j(this.p, w), 20),
          a(this.g(t, u, v), 3593408605, this.j(this.p, w + 20), 5),
          a(this.g(t, u, v), 38016083, this.j(this.p, w + 40), 9),
          a(this.g(t, u, v), 3634488961, this.j(this.p, w + 60), 14),
          a(this.g(t, u, v), 3889429448, this.j(this.p, w + 16), 20),
          a(this.g(t, u, v), 568446438, this.j(this.p, w + 36), 5),
          a(this.g(t, u, v), 3275163606, this.j(this.p, w + 56), 9),
          a(this.g(t, u, v), 4107603335, this.j(this.p, w + 12), 14),
          a(this.g(t, u, v), 1163531501, this.j(this.p, w + 32), 20),
          a(this.g(t, u, v), 2850285829, this.j(this.p, w + 52), 5),
          a(this.g(t, u, v), 4243563512, this.j(this.p, w + 8), 9),
          a(this.g(t, u, v), 1735328473, this.j(this.p, w + 28), 14),
          a(this.g(t, u, v), 2368359562, this.j(this.p, w + 48), 20),
          a(this.h(t, u, v), 4294588738, this.j(this.p, w + 20), 4),
          a(this.h(t, u, v), 2272392833, this.j(this.p, w + 32), 11),
          a(this.h(t, u, v), 1839030562, this.j(this.p, w + 44), 16),
          a(this.h(t, u, v), 4259657740, this.j(this.p, w + 56), 23),
          a(this.h(t, u, v), 2763975236, this.j(this.p, w + 4), 4),
          a(this.h(t, u, v), 1272893353, this.j(this.p, w + 16), 11),
          a(this.h(t, u, v), 4139469664, this.j(this.p, w + 28), 16),
          a(this.h(t, u, v), 3200236656, this.j(this.p, w + 40), 23),
          a(this.h(t, u, v), 681279174, this.j(this.p, w + 52), 4),
          a(this.h(t, u, v), 3936430074, this.j(this.p, w), 11),
          a(this.h(t, u, v), 3572445317, this.j(this.p, w + 12), 16),
          a(this.h(t, u, v), 76029189, this.j(this.p, w + 24), 23),
          a(this.h(t, u, v), 3654602809, this.j(this.p, w + 36), 4),
          a(this.h(t, u, v), 3873151461, this.j(this.p, w + 48), 11),
          a(this.h(t, u, v), 530742520, this.j(this.p, w + 60), 16),
          a(this.h(t, u, v), 3299628645, this.j(this.p, w + 8), 23),
          a(this.i(t, u, v), 4096336452, this.j(this.p, w), 6),
          a(this.i(t, u, v), 1126891415, this.j(this.p, w + 28), 10),
          a(this.i(t, u, v), 2878612391, this.j(this.p, w + 56), 15),
          a(this.i(t, u, v), 4237533241, this.j(this.p, w + 20), 21),
          a(this.i(t, u, v), 1700485571, this.j(this.p, w + 48), 6),
          a(this.i(t, u, v), 2399980690, this.j(this.p, w + 12), 10),
          a(this.i(t, u, v), 4293915773, this.j(this.p, w + 40), 15),
          a(this.i(t, u, v), 2240044497, this.j(this.p, w + 4), 21),
          a(this.i(t, u, v), 1873313359, this.j(this.p, w + 32), 6),
          a(this.i(t, u, v), 4264355552, this.j(this.p, w + 60), 10),
          a(this.i(t, u, v), 2734768916, this.j(this.p, w + 24), 15),
          a(this.i(t, u, v), 1309151649, this.j(this.p, w + 52), 21),
          a(this.i(t, u, v), 4149444226, this.j(this.p, w + 16), 6),
          a(this.i(t, u, v), 3174756917, this.j(this.p, w + 44), 10),
          a(this.i(t, u, v), 718787259, this.j(this.p, w + 8), 15),
          a(this.i(t, u, v), 3951481745, this.j(this.p, w + 36), 21),
          m = this.n(m, s),
          o = this.n(o, t),
          q = this.n(q, u),
          r = this.n(r, v)
      }
      return this.l(r, q, o, m).toUpperCase()
    }

    MD5(a) {
      this.p = null;
      this.q = null;
      return "string" == typeof a ? 
        this.p = this.k(a) : a.constructor == Array ? 
        0 === a.length ? this.p = a : "string" == typeof a[0] ? 
        this.p = this.c(a) : "number" == typeof a[0] ? 
        this.p = a : this.q = typeof a[0] : "undefined" != typeof ArrayBuffer ? 
        a instanceof ArrayBuffer ? this.p = this.m(new Uint8Array(a)) : a instanceof Uint8Array || a instanceof Int8Array ? 
        this.p = this.m(a) : a instanceof Uint32Array || a instanceof Int32Array || a instanceof Uint16Array || a instanceof Int16Array || a instanceof Float32Array || a instanceof Float64Array ? 
        this.p = this.m(new Uint8Array(a.buffer)) : this.q = typeof a : this.q = typeof a,
        this.q && alert("MD5 type mismatch, cannot process " + this.q),
        this.o();
    }
}
