<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Notifikasi;
use Illuminate\Http\Request;

class NotifikasiController extends Controller
{
    // daftar notifikasi user
    public function index(Request $request)
    {
        $userId = $request->user()->id;

        $notif = Notifikasi::where('user_id', $userId)
            ->orderByDesc('created_at')
            ->limit(100)
            ->get()
            ->map(function ($n) {
                return [
                    'id'      => $n->id,
                    'judul'   => $n->judul,
                    'pesan'   => $n->pesan,
                    'dibaca'  => $n->dibaca,
                    'tanggal' => $n->created_at->format('Y-m-d H:i:s'),
                ];
            });

        $belumDibaca = Notifikasi::where('user_id', $userId)
            ->where('dibaca', false)
            ->count();

        return response()->json([
            'success' => true,
            'message' => 'Daftar Notifikasi',
            'data'    => [
                'jumlah_belum_dibaca' => $belumDibaca,
                'notifikasi'          => $notif,
            ],
        ]);
    }

    // tandai sudah dibaca
    public function bacaSemua(Request $request)
    {
        Notifikasi::where('user_id', $request->user()->id)
            ->where('dibaca', false)
            ->update(['dibaca' => true]);

        return response()->json([
            'success' => true,
            'message' => 'Semua notifikasi ditandai dibaca.',
        ]);
    }
}
